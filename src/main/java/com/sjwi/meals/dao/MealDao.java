/* (C)2022 sjwi */
package com.sjwi.meals.dao;

import static com.sjwi.meals.model.security.MealsUser.USER_PREFERENCE_KEYS;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import com.sjwi.meals.model.Ingredient;
import com.sjwi.meals.model.Meal;
import com.sjwi.meals.model.Side;
import com.sjwi.meals.model.SqlQueryStore;
import com.sjwi.meals.model.Week;
import com.sjwi.meals.model.WeekMeal;
import com.sjwi.meals.model.security.MealsUser;

@Repository
public class MealDao {

  @Value("${meals.files.urlBase}")
  String imageUrlBase;

  @Autowired private SqlQueryStore queryStore;

  @Autowired JdbcTemplate jdbcTemplate;

  @Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private final Calendar tzCal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));

  public List<Meal> getAllMeals(Map<String, String> preferences) {
    return jdbcTemplate.query(
        queryStore.get("getAllMeals", preferences),
        new Object[] {getSuperUsername()},
        r -> {
          List<Meal> meals = new ArrayList<>();
          while (r.next()) {
            meals.add(buildMealFromResultSet(r));
          }
          return meals;
        });
  }

  public List<Meal> searchMeals(
      String searchTerm, List<Integer> tags, Map<String, String> preferences) {
    String query =
        queryStore
            .get("searchMeals")
            .replace("{{ADDITIONAL_PARAMS}}", tags.size() == 0 ? "" : "AND mt.TAG_ID in (:tags)");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("term", "%" + searchTerm + "%");
    parameters.put("tags", tags);
    parameters.put("user", getSuperUsername());
    List<Integer> mealIds =
        namedParameterJdbcTemplate.query(
            query,
            parameters,
            r -> {
              List<Integer> mIds = new ArrayList<>();
              while (r.next()) {
                mIds.add(r.getInt("ID"));
              }
              return mIds;
            });
    return getMealsByIds(mealIds, preferences);
  }

  public List<Week> getAllWeeks() {
    return jdbcTemplate.query(
        queryStore.get("getAllWeeks"),
        new Object[] {getSuperUsername()},
        r -> {
          List<Week> weeks = new ArrayList<>();
          while (r.next()) {
            List<WeekMeal> meals = getMealsInWeek(r.getInt("ID"));
            weeks.add(
                new Week(
                    r.getInt("ID"),
                    r.getDate("DATE_BEGIN", tzCal),
                    r.getDate("DATE_END", tzCal),
                    meals));
          }
          return weeks;
        });
  }

  public List<Week> getNNumberOfWeeks(int n) {
    return jdbcTemplate.query(
        queryStore.get("getNNumberOfWeeks"),
        new Object[] {getSuperUsername(), n},
        r -> {
          List<Week> weeks = new ArrayList<>();
          while (r.next()) {
            List<WeekMeal> meals = getMealsInWeek(r.getInt("ID"));
            weeks.add(
                new Week(
                    r.getInt("ID"),
                    r.getDate("DATE_BEGIN", tzCal),
                    r.getDate("DATE_END", tzCal),
                    meals));
          }
          return weeks;
        });
  }

  public Map<Integer, String> getAllTags() {
    return jdbcTemplate.query(
        queryStore.get("getAllTags"),
        new Object[] {getSuperUsername()},
        r -> {
          Map<Integer, String> tags = new HashMap<>();
          while (r.next()) tags.put(r.getInt("ID"), r.getString("NAME"));
          return tags;
        });
  }

  public List<Ingredient> getAllIngredients() {
    SqlParameterSource parameters = new MapSqlParameterSource("user", getSuperUsername());
    return namedParameterJdbcTemplate.query(
        queryStore.get("getAllIngredients"),
        parameters,
        r -> {
          List<Ingredient> ingredients = new ArrayList<>();
          while (r.next()) {
            ingredients.add(new Ingredient(r.getInt("ID"), r.getString("NAME")));
          }
          return ingredients;
        });
  }

  public Meal getMealById(int id) {
    return jdbcTemplate.query(
        queryStore.get("getMealById"),
        new Object[] {id},
        r -> {
          r.next();
          return buildMealFromResultSet(r);
        });
  }

  public List<Meal> getMealsByIds(List<Integer> mealIds, Map<String, String> preferences) {
    if (mealIds.size() > 0) {
      SqlParameterSource parameters = new MapSqlParameterSource("mealIds", mealIds);
      return namedParameterJdbcTemplate.query(
          queryStore.get("getMealsByIds", preferences),
          parameters,
          r -> {
            List<Meal> meals = new ArrayList<>();
            while (r.next()) meals.add(buildMealFromResultSet(r));
            return meals;
          });
    } else return new ArrayList<Meal>();
  }

  public Integer getLatestWeekId() {
    return jdbcTemplate.query(
        queryStore.get("getLatestWeekId"),
        new Object[] {getSuperUsername()},
        r -> {
          r.next();
          return r.getInt("ID");
        });
  }

  public Integer getLatestMealId() {
    return jdbcTemplate.query(
        queryStore.get("getLatestMealId"),
        new Object[] {getSuperUsername()},
        r -> {
          r.next();
          return r.getInt("ID");
        });
  }

  public Integer createWeek(Date start, Date end) {
    jdbcTemplate.update(queryStore.get("createWeek"), new Object[] {start, end, getUsername()});
    return getLatestWeekId();
  }

  public void removeMealFromWeek(int mealId, int weekId) {
    jdbcTemplate.update(queryStore.get("removeMealFromWeek"), new Object[] {mealId, weekId});
  }

  public void deleteWeek(int weekId) {
    jdbcTemplate.update(queryStore.get("deleteWeek"), new Object[] {weekId});
  }

  public void deleteMeal(int mealId) {
    jdbcTemplate.update(queryStore.get("deleteMeal"), new Object[] {mealId});
    jdbcTemplate.update(queryStore.get("deleteMealTags"), new Object[] {mealId});
    jdbcTemplate.update(queryStore.get("deleteMealIngredients"), new Object[] {mealId});
  }

  public Week getWeekById(int id) {
    return jdbcTemplate.query(
        queryStore.get("getWeekById"),
        new Object[] {id},
        r -> {
          r.next();
          List<WeekMeal> meals = getMealsInWeek(r.getInt("ID"));
          return new Week(
              r.getInt("ID"), r.getDate("DATE_BEGIN", tzCal), r.getDate("DATE_END", tzCal), meals);
        });
  }

  public int createMeal(
      String name,
      Boolean favorite,
      Set<Integer> ingredients,
      Set<Integer> tags,
      String notes,
      String recipeUrl) {
    jdbcTemplate.update(
        queryStore.get("createMeal"),
        new Object[] {name, favorite, notes, recipeUrl, getUsername()});
    int mealId = getLatestMealId();
    addIngredientsToMeal(ingredients, mealId);
    addTagsToMeal(tags, mealId);
    return mealId;
  }

  public void addMealsToWeek(Integer weekId, List<Integer> mealIds) {
    jdbcTemplate.batchUpdate(
        queryStore.get("addMealToWeek"),
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            Integer mealId = mealIds.get(i);
            ps.setInt(1, weekId);
            ps.setInt(2, mealId);
          }

          @Override
          public int getBatchSize() {
            return mealIds.size();
          }
        });
  }

  public void removeIngredientsFromSide(Set<Integer> ingredientsToDelete, Integer sideId) {
    if (ingredientsToDelete.size() > 0) {
      List<Integer> ingredientsToDeleteList = new ArrayList<>(ingredientsToDelete);
      jdbcTemplate.batchUpdate(
          queryStore.get("deleteIngredientsInSide"),
          new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setInt(1, ingredientsToDeleteList.get(i));
              ps.setInt(2, sideId);
            }

            @Override
            public int getBatchSize() {
              return ingredientsToDeleteList.size();
            }
          });
    }
  }

  public void removeIngredientsFromMeal(Set<Integer> ingredientsToDelete, Integer mealId) {
    if (ingredientsToDelete.size() > 0) {
      List<Integer> ingredientsToDeleteList = new ArrayList<>(ingredientsToDelete);
      jdbcTemplate.batchUpdate(
          queryStore.get("deleteIngredientsInMeal"),
          new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setInt(1, ingredientsToDeleteList.get(i));
              ps.setInt(2, mealId);
            }

            @Override
            public int getBatchSize() {
              return ingredientsToDeleteList.size();
            }
          });
    }
  }

  public void removeTagsFromMeal(Set<Integer> tagsToDelete, Integer mealId) {
    if (tagsToDelete.size() > 0) {
      List<Integer> tagsToDeleteList = new ArrayList<>(tagsToDelete);
      jdbcTemplate.batchUpdate(
          queryStore.get("deleteTagsInMeal"),
          new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
              ps.setInt(1, tagsToDeleteList.get(i));
              ps.setInt(2, mealId);
            }

            @Override
            public int getBatchSize() {
              return tagsToDeleteList.size();
            }
          });
    }
  }

  public void addIngredientsToSide(Set<Integer> ingredients, int sideId) {
    List<Integer> ingredientsToAdd = new ArrayList<>(ingredients);
    jdbcTemplate.batchUpdate(
        queryStore.get("insertSideIngredients"),
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setInt(1, ingredientsToAdd.get(i));
            ps.setInt(2, sideId);
          }

          @Override
          public int getBatchSize() {
            return ingredientsToAdd.size();
          }
        });
  }

  public void addIngredientsToMeal(Set<Integer> ingredients, int mealId) {
    List<Integer> ingredientsToAdd = new ArrayList<>(ingredients);
    jdbcTemplate.batchUpdate(
        queryStore.get("insertMealIngredients"),
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setInt(1, ingredientsToAdd.get(i));
            ps.setInt(2, mealId);
          }

          @Override
          public int getBatchSize() {
            return ingredientsToAdd.size();
          }
        });
  }

  public void addTagsToMeal(Set<Integer> tags, int mealId) {
    List<Integer> tagsToAdd = new ArrayList<>(tags);
    jdbcTemplate.batchUpdate(
        queryStore.get("insertMealTags"),
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setInt(1, tagsToAdd.get(i));
            ps.setInt(2, mealId);
          }

          @Override
          public int getBatchSize() {
            return tagsToAdd.size();
          }
        });
  }

  public void editMeal(
      int mealId,
      String inputMealName,
      Boolean favorite,
      String notes,
      String recipeUrl,
      Set<Integer> ingredients,
      Set<Integer> tags,
      Set<Integer> ingredientsToDelete,
      Set<Integer> tagsToDelete) {

    jdbcTemplate.update(
        queryStore.get("updateMeal"),
        new Object[] {inputMealName, favorite, notes, recipeUrl, mealId});

    removeIngredientsFromMeal(ingredientsToDelete, mealId);
    removeTagsFromMeal(tagsToDelete, mealId);
    addIngredientsToMeal(ingredients, mealId);
    addTagsToMeal(tags, mealId);
  }

  public void editSide(
      int sideId,
      String inputSideName,
      String notes,
      String recipeUrl,
      Set<Integer> ingredientsToAdd,
      Set<Integer> ingredientsToDelete) {
    jdbcTemplate.update(
        queryStore.get("updateSide"), new Object[] {inputSideName, notes, recipeUrl, sideId});
    removeIngredientsFromSide(ingredientsToDelete, sideId);
    addIngredientsToSide(ingredientsToAdd, sideId);
  }

  public Set<Integer> createIngredients(List<String> ingredientsToCreate) {
    jdbcTemplate.batchUpdate(
        queryStore.get("createIngredient"),
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, ingredientsToCreate.get(i));
            ps.setString(2, getUsername());
          }

          @Override
          public int getBatchSize() {
            return ingredientsToCreate.size();
          }
        });
    return jdbcTemplate.query(
        queryStore.get("getNMostRecentIngredients"),
        new Object[] {getSuperUsername(), ingredientsToCreate.size()},
        r -> {
          Set<Integer> createdIngredientIds = new HashSet<>();
          while (r.next()) {
            createdIngredientIds.add(r.getInt("ID"));
          }
          return createdIngredientIds;
        });
  }

  public Set<Integer> createTags(List<String> tagsToCreate) {
    jdbcTemplate.batchUpdate(
        queryStore.get("createTag"),
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, tagsToCreate.get(i));
            ps.setString(2, getUsername());
          }

          @Override
          public int getBatchSize() {
            return tagsToCreate.size();
          }
        });
    return jdbcTemplate.query(
        queryStore.get("getNMostRecentTags"),
        new Object[] {getSuperUsername(), tagsToCreate.size()},
        r -> {
          Set<Integer> createdTagIds = new HashSet<>();
          while (r.next()) {
            createdTagIds.add(r.getInt("ID"));
          }
          return createdTagIds;
        });
  }

  public User getUser(String username) {
    Map<String, String> parameters = new HashMap<>();
    parameters.put("username", username);
    return namedParameterJdbcTemplate.query(
        queryStore.get("getUser"),
        parameters,
        r -> {
          if (r.next()) {
            Map<String, String> userPreferences = new HashMap<>();
            for (String key : USER_PREFERENCE_KEYS) userPreferences.put(key, r.getString(key));
            return new MealsUser(
                r.getString("username"),
                r.getString("firstname"),
                r.getString("lastname"),
                r.getString("email"),
                r.getString("refreshToken"),
                getUserAuthorities(username),
                userPreferences);
          } else {
            return null;
          }
        });
  }

  public void toggleFavorite(int id, boolean toggle) {
    jdbcTemplate.update(queryStore.get("toggleFavorite"), new Object[] {toggle ? 1 : 0, id});
  }

  public void storeCookieToken(String username, String token) {
    jdbcTemplate.update(queryStore.get("storeCookieToken"), new Object[] {username, token});
  }

  public SimpleEntry<String, String> getStoredCookieToken(String token) {
    return jdbcTemplate.query(
        queryStore.get("getUserCookieToken"),
        new Object[] {token},
        r -> {
          if (r.next())
            return new SimpleEntry<String, String>(
                r.getString("username"), r.getString("LOGIN_COOKIE"));
          else return new SimpleEntry<String, String>(null, null);
        });
  }

  private List<WeekMeal> getMealsInWeek(int weekId) {
    return jdbcTemplate.query(
        queryStore.get("getAllMealsInWeek"),
        new Object[] {weekId},
        r -> {
          List<WeekMeal> meals = new ArrayList<>();
          while (r.next()) {
            meals.add(buildWeekMealFromResultSet(r, weekId));
          }
          return meals;
        });
  }

  private List<Side> getMealSides(int mealId, int weekId) {
    return jdbcTemplate.query(
        queryStore.get("getMealSides"),
        new Object[] {mealId, weekId},
        r -> {
          List<Side> sides = new ArrayList<>();
          while (r.next()) sides.add(buildSideFromResultSet(r));
          return sides;
        });
  }

  private Side buildSideFromResultSet(ResultSet r) throws SQLException {
    return new Side(
        r.getInt("ID"),
        r.getString("NAME"),
        r.getString("NOTES"),
        r.getString("RECIPE_URL"),
        getIngredientsInSide(r.getInt("ID")));
  }

  private Meal buildMealFromResultSet(ResultSet rs) throws SQLException {
    List<Ingredient> ingredients = getIngredientsInMeal(rs.getInt("ID"));
    Map<Integer, String> tags = getMealTags(rs.getInt("ID"));
    return new Meal(
        rs.getInt("ID"),
        rs.getString("NAME"),
        rs.getInt("CNT"),
        rs.getDate("LAST_EATEN", tzCal),
        rs.getBoolean("FAVORITE"),
        rs.getString("RECIPE_URL"),
        rs.getString("NOTES"),
        ingredients,
        tags);
  }

  private Map<Integer, String> getMealTags(int mealId) {
    return jdbcTemplate.query(
        queryStore.get("getMealTags"),
        new Object[] {mealId},
        r -> {
          Map<Integer, String> tags = new HashMap<>();
          while (r.next()) tags.put(r.getInt("TAG_ID"), r.getString("NAME"));
          return tags;
        });
  }

  private List<Ingredient> getIngredientsInSide(int sideId) {
    return jdbcTemplate.query(
        queryStore.get("getIngredientsInSide"),
        new Object[] {sideId},
        r -> {
          List<Ingredient> ingredients = new ArrayList<>();
          while (r.next()) ingredients.add(new Ingredient(r.getInt("ID"), r.getString("NAME")));
          return ingredients;
        });
  }

  private List<Ingredient> getIngredientsInMeal(int mealId) {
    return jdbcTemplate.query(
        queryStore.get("getIngredientsInMeal"),
        new Object[] {mealId},
        r -> {
          List<Ingredient> ingredients = new ArrayList<>();
          while (r.next()) ingredients.add(new Ingredient(r.getInt("ID"), r.getString("NAME")));
          return ingredients;
        });
  }

  private Collection<? extends GrantedAuthority> getUserAuthorities(String username) {
    return jdbcTemplate.query(
        queryStore.get("getAuthoritiesByUsername"),
        new Object[] {username},
        r -> {
          List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
          while (r.next()) {
            authorities.add(new SimpleGrantedAuthority(r.getString("authority")));
          }
          return authorities;
        });
  }

  public void updatePreferences(
      boolean pinFavorites,
      String sortBy,
      String sortOrder,
      int weekStartDay,
      String locationId,
      String username) {
    jdbcTemplate.update(
        queryStore.get("updateUserPreferences"),
        new Object[] {pinFavorites, sortBy, sortOrder, weekStartDay, locationId, username});
  }

  public List<Side> getAllSides() {
    return jdbcTemplate.query(
        queryStore.get("getAllSides"),
        new Object[] {getSuperUsername()},
        r -> {
          List<Side> sides = new ArrayList<>();
          while (r.next()) {
            sides.add(buildSideFromResultSet(r));
          }
          return sides;
        });
  }

  public void createSide(String inputSideName, Set<Integer> createdIngredients) {
    jdbcTemplate.update(queryStore.get("createSide"), new Object[] {inputSideName, getUsername()});
    int sideId = getLatestSideId();
    addIngredientsToSide(createdIngredients, sideId);
  }

  private int getLatestSideId() {
    return jdbcTemplate.query(
        queryStore.get("getLatestSideId"),
        new Object[] {getSuperUsername()},
        r -> {
          r.next();
          return r.getInt("ID");
        });
  }

  public Side getSideById(int id) {
    return jdbcTemplate.query(
        queryStore.get("getSideById"),
        new Object[] {id},
        r -> {
          r.next();
          return buildSideFromResultSet(r);
        });
  }

  public void deleteSide(int sideId) {
    jdbcTemplate.update(queryStore.get("deleteSide"), new Object[] {sideId});
  }

  public void addSidesToMeals(
      List<Integer> mealIds, Map<Integer, List<Integer>> sidesMap, Integer weekId) {
    for (Integer mealId : mealIds)
      if (sidesMap.containsKey(mealId)) addSidesToMeal(mealId, weekId, sidesMap.get(mealId));
  }

  public void addSidesToMeal(Integer mealId, Integer weekId, List<Integer> sideList) {
    jdbcTemplate.batchUpdate(
        queryStore.get("addSideToMeal"),
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setInt(1, mealId);
            ps.setInt(2, weekId);
            ps.setObject(3, sideList.get(i));
          }

          @Override
          public int getBatchSize() {
            return sideList.size();
          }
        });
  }

  public WeekMeal getWeekMealById(int id, int weekId) {
    return jdbcTemplate.query(
        queryStore.get("getWeekMealById"),
        new Object[] {id, weekId},
        r -> {
          r.next();
          return buildWeekMealFromResultSet(r, weekId);
        });
  }

  private WeekMeal buildWeekMealFromResultSet(ResultSet r, int weekId) throws SQLException {
    List<Ingredient> ingredients = getIngredientsInMeal(r.getInt("ID"));
    Map<Integer, String> tags = getMealTags(r.getInt("ID"));
    List<Side> sides = getMealSides(r.getInt("ID"), weekId);
    List<Ingredient> combinedIngredients =
        sides.stream()
            .map(s -> s.getIngredients())
            .flatMap(List::stream)
            .collect(Collectors.toList());
    combinedIngredients.addAll(ingredients);
    Map<Ingredient, Integer> ingredientMap = new HashMap<>();
    combinedIngredients.forEach(
        i -> {
          if (ingredientMap.containsKey(i)) ingredientMap.put(i, ingredientMap.get(i) + 1);
          else ingredientMap.put(i, 1);
        });
    return new WeekMeal(
        r.getInt("ID"),
        r.getString("NAME"),
        r.getString("RECIPE_URL"),
        r.getString("NOTES"),
        ingredientMap,
        tags,
        sides);
  }

  public void removeSideFromWeekMeal(int sideId, int mealId, int weekId) {
    jdbcTemplate.update(
        queryStore.get("removeSideFromWeekMeal"), new Object[] {mealId, weekId, sideId});
  }

  private String getUsername() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  private String getSuperUsername() {
    if (userHasAuthority("ADMIN")) return "%";
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  public static boolean userHasAuthority(String authority) {
    return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
        .anyMatch(ga -> ga.getAuthority().equals("ADMIN"));
  }

  public void deleteCookieToken(String token) {
    jdbcTemplate.update(queryStore.get("deleteCookieToken"), new Object[] {token});
  }

  public List<Side> searchSides(String searchTerm) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("term", "%" + searchTerm + "%");
    parameters.put("user", getSuperUsername());
    return namedParameterJdbcTemplate.query(
        queryStore.get("searchSides"),
        parameters,
        r -> {
          List<Side> sides = new ArrayList<>();
          while (r.next()) sides.add(buildSideFromResultSet(r));
          return sides;
        });
  }

  public User registerNewUser(String oAuthUser, String refreshToken) {
    jdbcTemplate.update(queryStore.get("registerNewUser"), new Object[] {oAuthUser, refreshToken});
    jdbcTemplate.update(queryStore.get("addUserAuthorities"), new Object[] {oAuthUser});
    return getUser(oAuthUser);
  }

  public User updateUserRefreshToken(String oAuthUser, String refresh_token) {
    jdbcTemplate.update(
        queryStore.get("updateUserRefreshToken"), new Object[] {refresh_token, oAuthUser});
    return getUser(oAuthUser);
  }

  public Ingredient getIngredientById(int id) {
    return jdbcTemplate.query(
        queryStore.get("getIngredientById"),
        new Object[] {id},
        r -> {
          r.next();
          return new Ingredient(r.getInt("ID"), r.getString("NAME"));
        });
  }

  public Integer getIngredientIdByName(String inputSideName) {
    return jdbcTemplate.query(
        queryStore.get("getIngredientIdByName"),
        new Object[] {inputSideName, getSuperUsername()},
        r -> {
          if (r.next()) return r.getInt("ID");
          else return null;
        });
  }

  public Integer createIngredient(String inputSideName) {
    jdbcTemplate.update(
        queryStore.get("createIngredient"), new Object[] {inputSideName, getUsername()});
    return getLatestIngredientId();
  }

  private Integer getLatestIngredientId() {
    return jdbcTemplate.query(
        queryStore.get("getLatestIngredientId"),
        new Object[] {getSuperUsername()},
        r -> {
          r.next();
          return r.getInt("ID");
        });
  }

  public void setMealsFiles(List<String> fileNames, int mealId) {
    jdbcTemplate.batchUpdate(
        queryStore.get("addImageToMeal"),
        new BatchPreparedStatementSetter() {
          @Override
          public void setValues(PreparedStatement ps, int i) throws SQLException {
            ps.setString(1, fileNames.get(i));
            ps.setInt(2, mealId);
          }

          @Override
          public int getBatchSize() {
            return fileNames.size();
          }
        });
  }

  public void flagRecipeUrlSelfHosted(int mealId) {
    jdbcTemplate.update(queryStore.get("flagRecipeUrlSelfHosted"), new Object[] {mealId});
  }

  public List<String> getRecipeImagesForMeal(int mealId) {
    return jdbcTemplate.query(
        queryStore.get("getRecipeImagesForMeal"),
        new Object[] {mealId},
        r -> {
          List<String> imageUrls = new ArrayList<>();
          while (r.next()) imageUrls.add(imageUrlBase + "/" + r.getString("IMAGE_URL"));
          return imageUrls;
        });
  }

  public void updateUser(String username, String firstName, String lastName, String email) {
    jdbcTemplate.update(
        queryStore.get("updateUser"), new Object[] {firstName, lastName, email, username});
  }

  public void deleteAccount(String name) {
    jdbcTemplate.update(queryStore.get("deleteUserStoredLogins"), new Object[] {name});
    jdbcTemplate.update(queryStore.get("deleteUserAuthorities"), new Object[] {name});
    jdbcTemplate.update(queryStore.get("deleteUser"), new Object[] {name});
  }

  public void deleteUserWeeks(String name) {
    jdbcTemplate.update(queryStore.get("deleteUserPlannedWeeks"), new Object[] {name});
    jdbcTemplate.update(queryStore.get("deleteUserWeeks"), new Object[] {name});
  }

  public void deleteUserMeals(String name) {
    jdbcTemplate.update(queryStore.get("deleteUserMeals"), new Object[] {name});
  }

  public void deleteUserSides(String name) {
    jdbcTemplate.update(queryStore.get("deleteUserMealSides"), new Object[] {name});
    jdbcTemplate.update(queryStore.get("deleteUserSides"), new Object[] {name});
  }

  public void deleteUserIngredients(String name) {
    jdbcTemplate.update(queryStore.get("deleteUserSideIngredients"), new Object[] {name});
    jdbcTemplate.update(queryStore.get("deleteUserMealIngredients"), new Object[] {name});
    jdbcTemplate.update(queryStore.get("deleteUserIngredients"), new Object[] {name});
  }

  public void deleteUserTags(String name) {
    jdbcTemplate.update(queryStore.get("deleteUserMealTags"), new Object[] {name});
    jdbcTemplate.update(queryStore.get("deleteUserTags"), new Object[] {name});
  }

  @Async
  public void log(
      String username, String os, String signature, String requestUrl, String parameters) {
    jdbcTemplate.update(
        queryStore.get("log"), new Object[] {username, os, signature, requestUrl, parameters});
  }

  public void moveWeekMeal(Integer mealId, Integer weekId, Integer oldWeekId) {
    Map<String, Integer> params =
        Map.of("mealId", mealId, "weekId", weekId, "oldWeekId", oldWeekId);
    namedParameterJdbcTemplate.update(queryStore.get("moveWeekMeal"), params);
  }
}
