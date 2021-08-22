package com.sjwi.meals.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sjwi.meals.model.Ingredient;
import com.sjwi.meals.model.Meal;
import com.sjwi.meals.model.Week;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MealDao {
  @Autowired
  private Map<String, String> queryStore;

  @Autowired
  JdbcTemplate jdbcTemplate;

  public List<Meal> getAllMeals() {
    return jdbcTemplate.query(queryStore.get("getAllMeals"), r -> {
      List<Meal> meals = new ArrayList<>();
      while (r.next()) {
        meals.add(buildMealFromResultSet(r));
      }
      return meals;
    });
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

  public int createMeal(String name, Boolean favorite, List<Integer> ingredients,
      List<Integer> tags, String notes, String recipeUrl) {
    jdbcTemplate.update(queryStore.get("createMeal"), new Object[] {name,favorite,notes,recipeUrl});
    int mealId = getLatestMealId();
    if (ingredients != null) {
      jdbcTemplate.batchUpdate(queryStore.get("addIngredientsToMeal"), new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
          ps.setInt(1, ingredients.get(i));
          ps.setInt(2, mealId);
        }

        @Override
        public int getBatchSize() {
          return ingredients.size();
        }
      });
    }
    if (tags != null) {
      jdbcTemplate.batchUpdate(queryStore.get("addTagsToMeal"), new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
          ps.setInt(1, tags.get(i));
          ps.setInt(2, mealId);
        }

        @Override
        public int getBatchSize() {
          return tags.size();
        }
      });
    }
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

  public void editMeal(int mealId, String inputMealName, Boolean favorite, String notes, String recipeUrl,
      Set<Integer> ingredients, Set<Integer> tags, Set<Integer> ingredientsToDelete, Set<Integer> tagsToDelete) {
    jdbcTemplate.update(queryStore.get("updateMeal"), new Object[] {inputMealName,favorite,notes,recipeUrl,mealId});
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
    Date lastEaten = getMealLastEaten(rs.getInt("ID"));
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

}