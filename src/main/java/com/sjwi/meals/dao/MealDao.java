package com.sjwi.meals.dao;

import static com.sjwi.meals.model.MealsUser.USER_PREFERENCE_KEYS;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sjwi.meals.model.Ingredient;
import com.sjwi.meals.model.Meal;
import com.sjwi.meals.model.MealsUser;
import com.sjwi.meals.model.SqlQueryStore;
import com.sjwi.meals.model.Week;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

@Repository
public class MealDao {

  @Autowired
  private SqlQueryStore queryStore;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public List<Meal> getAllMeals(Map<String,String> preferences) {
    return jdbcTemplate.query(queryStore.get("getAllMeals",preferences), r -> {
      List<Meal> meals = new ArrayList<>();
      while (r.next()) {
        meals.add(buildMealFromResultSet(r));
      }
      return meals;
    });
  }

  public List<Meal> searchMeals(String searchTerm, List<Integer> tags, Map<String, String> preferences) {
    String query = queryStore.get("searchMeals")
      .replace("{{ADDITIONAL_PARAMS}}", tags.size() == 0? "":"AND mt.TAG_ID in (:tags)");
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("term","%" + searchTerm + "%");
		parameters.put("tags", tags);
    List<Integer> mealIds = namedParameterJdbcTemplate.query(query, parameters, r -> {
      List<Integer> mIds = new ArrayList<>();
      while (r.next()) {
        mIds.add(r.getInt("ID"));
      }
      return mIds;
    });
    return getMealsByIds(mealIds, preferences);
  }

  public List<Week> getAllWeeks() {
    return jdbcTemplate.query(queryStore.get("getAllWeeks"), r -> {
      List<Week> weeks = new ArrayList<>();
      while (r.next()) {
        List<Meal> meals = getMealsInWeek(r.getInt("ID"));
        weeks.add(new Week(r.getInt("ID"), r.getDate("DATE_BEGIN"), r.getDate("DATE_END"), meals));
      }
      return weeks;
    });
  }

  public Map<Integer, String> getAllTags() {
    return jdbcTemplate.query(queryStore.get("getAllTags"), r -> {
      Map<Integer, String> tags = new HashMap<>();
      while (r.next())
        tags.put(r.getInt("ID"), r.getString("NAME"));
      return tags;
    });
  }

  public List<Ingredient> getAllIngredients() {
    return jdbcTemplate.query(queryStore.get("getAllIngredients"), r -> {
      List<Ingredient> ingredients = new ArrayList<>();
      while (r.next()) {
        ingredients.add(new Ingredient(r.getInt("ID"), r.getString("NAME")));
      }
      return ingredients;
    });
  }

  public Meal getMealById(int id) {
    return jdbcTemplate.query(queryStore.get("getMealById"), new Object[] { id }, r -> {
      r.next();
      return buildMealFromResultSet(r);
    });
  }

  public List<Meal> getMealsByIds(List<Integer> mealIds, Map<String,String> preferences) {
    if (mealIds.size() > 0) {
      SqlParameterSource parameters = new MapSqlParameterSource("mealIds", mealIds);
      return namedParameterJdbcTemplate.query(queryStore.get("getMealsByIds",preferences), parameters, r -> {
        List<Meal> meals = new ArrayList<>();
        while (r.next())
          meals.add(buildMealFromResultSet(r));
        return meals;
      });
    }
    else
      return new ArrayList<Meal>();
  }

  public Integer getLatestWeekId() {
    return jdbcTemplate.query(queryStore.get("getLatestWeekId"), r -> {
      r.next();
      return r.getInt("ID");
    });
  }

  public Integer getLatestMealId() {
    return jdbcTemplate.query(queryStore.get("getLatestMealId"), r -> {
      r.next();
      return r.getInt("ID");
    });
  }

  public Integer createWeek(Date start, Date end) {
    jdbcTemplate.update(queryStore.get("createWeek"), new Object[] { start, end });
    return getLatestWeekId();
  }

  public void removeMealFromWeek(int mealId, int weekId) {
    jdbcTemplate.update(queryStore.get("removeMealFromWeek"), new Object[] { mealId, weekId });
  }

  public void deleteWeek(int weekId) {
    jdbcTemplate.update(queryStore.get("deleteWeek"), new Object[] { weekId });
  }

  public void deleteMeal(int mealId) {
    jdbcTemplate.update(queryStore.get("deleteMeal"), new Object[] { mealId });
  }

  public Week getWeekById(int id) {
    return jdbcTemplate.query(queryStore.get("getWeekById"), new Object[] { id }, r -> {
      r.next();
      List<Meal> meals = getMealsInWeek(r.getInt("ID"));
      return new Week(r.getInt("ID"), r.getDate("DATE_BEGIN"), r.getDate("DATE_END"), meals);
    });
  }

  public int createMeal(String name, Boolean favorite, Set<Integer> ingredients,
      Set<Integer> tags, String notes, String recipeUrl) {
    jdbcTemplate.update(queryStore.get("createMeal"), new Object[] {name,favorite,notes,recipeUrl});
    int mealId = getLatestMealId();
    addIngredientsToMeal(ingredients, mealId);
    addTagsToMeal(tags, mealId);
    return mealId;
  }

  public void addMealsToWeek(Integer weekId, List<Integer> mealIds) {
    jdbcTemplate.batchUpdate(queryStore.get("addMealToWeek"), new BatchPreparedStatementSetter() {
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

  public void removeIngredientsFromMeal(Set<Integer> ingredientsToDelete, Integer mealId) {
    if (ingredientsToDelete.size() > 0) {
      List<Integer> ingredientsToDeleteList = new ArrayList<>(ingredientsToDelete);
      jdbcTemplate.batchUpdate(queryStore.get("deleteIngredientsInMeal"), new BatchPreparedStatementSetter() {
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
      jdbcTemplate.batchUpdate(queryStore.get("deleteTagsInMeal"), new BatchPreparedStatementSetter() {
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

  public void addIngredientsToMeal(Set<Integer> ingredients, int mealId) {
    List<Integer> ingredientsToAdd = new ArrayList<>(ingredients);
    jdbcTemplate.batchUpdate(queryStore.get("insertMealIngredients"), new BatchPreparedStatementSetter() {
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
    jdbcTemplate.batchUpdate(queryStore.get("insertMealTags"), new BatchPreparedStatementSetter() {
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

  public void editMeal(int mealId, String inputMealName, Boolean favorite, String notes, String recipeUrl,
      Set<Integer> ingredients, Set<Integer> tags, Set<Integer> ingredientsToDelete, Set<Integer> tagsToDelete) {

    jdbcTemplate.update(queryStore.get("updateMeal"), new Object[] {inputMealName,favorite,notes,recipeUrl,mealId});

    removeIngredientsFromMeal(ingredientsToDelete, mealId);
    removeTagsFromMeal(tagsToDelete, mealId);
    addIngredientsToMeal(ingredients, mealId);
    addTagsToMeal(tags, mealId);
  }

  public Set<Integer> createIngredients(List<String> ingredientsToCreate) {
    jdbcTemplate.batchUpdate(queryStore.get("createIngredient"), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setString(1, ingredientsToCreate.get(i));
      }
      @Override
      public int getBatchSize() {
        return ingredientsToCreate.size();
      }
    });
    return jdbcTemplate.query(queryStore.get("getNMostRecentIngredients"), new Object[] {ingredientsToCreate.size()}, r -> {
      Set<Integer> createdIngredientIds = new HashSet<>();
      while (r.next()) {
        createdIngredientIds.add(r.getInt("ID"));
      }
      return createdIngredientIds;
    });
  }

  public Set<Integer> createTags(List<String> tagsToCreate) {
    jdbcTemplate.batchUpdate(queryStore.get("createTag"), new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        ps.setString(1, tagsToCreate.get(i));
      }
      @Override
      public int getBatchSize() {
        return tagsToCreate.size();
      }
    });
    return jdbcTemplate.query(queryStore.get("getNMostRecentTags"), new Object[] {tagsToCreate.size()}, r -> {
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
		return namedParameterJdbcTemplate.query(queryStore.get("getUser"),parameters,r -> {
      r.next();
      Map<String,String> userPreferences = new HashMap<>();
      for (String key: USER_PREFERENCE_KEYS)
        userPreferences.put(key,r.getString(key));
      return new MealsUser(r.getString("username"),
          r.getString("firstname"),
          r.getString("lastname"),
          r.getString("email"),
          r.getString("password"),
          getUserAuthorities(username),
          userPreferences);
		});
	}

  public void toggleFavorite(int id, boolean toggle) {
    jdbcTemplate.update(queryStore.get("toggleFavorite"), new Object[]{toggle? 1: 0, id});
  }

  public void storeCookieToken(String username, String token) {
		jdbcTemplate.update(queryStore.get("storeCookieToken"), new Object[] {username,token});
	}

  public SimpleEntry<String, String> getStoredCookieToken(String token) {
    return jdbcTemplate.query(queryStore.get("getUserCookieToken"), new Object[] {token}, r -> {
      if (r.next())
        return new SimpleEntry<String, String> (r.getString("username"),r.getString("LOGIN_COOKIE"));
      else
        return new SimpleEntry<String, String> (null,null);
    });
  }

  private List<Meal> getMealsInWeek(int weekId) {
    return jdbcTemplate.query(queryStore.get("getAllMealsInWeek"), new Object[] { weekId }, r -> {
      List<Meal> meals = new ArrayList<>();
      while (r.next()) {
        List<Ingredient> ingredients = getIngredientsInMeal(r.getInt("ID"));
        Date lastEaten = getMealLastEaten(r.getInt("ID"));
        Map<Integer, String> tags = getMealTags(r.getInt("ID"));
        meals.add(new Meal(r.getInt("ID"), r.getString("NAME"), 0, lastEaten, false, r.getString("RECIPE_URL"),
            r.getString("NOTES"), ingredients, tags));
      }
      return meals;
    });
  }

  private Meal buildMealFromResultSet(ResultSet rs) throws SQLException {
    List<Ingredient> ingredients = getIngredientsInMeal(rs.getInt("ID"));
    Map<Integer, String> tags = getMealTags(rs.getInt("ID"));
    return new Meal(rs.getInt("ID"), rs.getString("NAME"), rs.getInt("CNT"), rs.getDate("LAST_EATEN"),
            rs.getBoolean("FAVORITE"), rs.getString("RECIPE_URL"), rs.getString("NOTES"), ingredients, tags);
  }

  private Map<Integer, String> getMealTags(int mealId) {
    return jdbcTemplate.query(queryStore.get("getMealTags"), new Object[] { mealId }, r -> {
      Map<Integer, String> tags = new HashMap<>();
      while (r.next())
        tags.put(r.getInt("TAG_ID"), r.getString("NAME"));
      return tags;
    });
  }

  private Date getMealLastEaten(int mealId) {
    return jdbcTemplate.query(queryStore.get("getMealLastEaten"), new Object[] { mealId }, r -> {
      if (r.next())
        return r.getDate("LAST_EATEN");
      else
        return null;
    });
  }

  private List<Ingredient> getIngredientsInMeal(int mealId) {
    return jdbcTemplate.query(queryStore.get("getIngredientsInMeal"), new Object[] { mealId }, r -> {
      List<Ingredient> ingredients = new ArrayList<>();
      while (r.next())
        ingredients.add(new Ingredient(r.getInt("ID"), r.getString("NAME")));
      return ingredients;
    });
  }

  private Collection<? extends GrantedAuthority> getUserAuthorities(String username) {
		return jdbcTemplate.query(queryStore.get("getAuthoritiesByUsername"), new Object[] {username}, r -> {
			List <SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
			while (r.next()) {
				authorities.add(new SimpleGrantedAuthority(r.getString("authority")));
			}
			return authorities;
		});
	}

  public void updatePreferences(boolean pinFavorites, String sortBy, String sortOrder, String username) {
    jdbcTemplate.update(queryStore.get("updateUserPreferences"), new Object[] {pinFavorites, sortBy, sortOrder, username});
  }
}
 