package com.sjwi.meals.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sjwi.meals.model.Ingredient;
import com.sjwi.meals.model.Meal;
import com.sjwi.meals.model.Week;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MealDao {
    @Autowired
	private Map<String,String> queryStore;
	
	@Autowired
	JdbcTemplate jdbcTemplate; 

    public List<Meal> getAllMeals() {
        return jdbcTemplate.query(queryStore.get("getAllMeals"), r -> {
            List<Meal> meals = new ArrayList<>();
            while (r.next()) {
                List<Ingredient> ingredients = getIngredientsInMeal(r.getInt("ID"));
                Map<Integer,String> tags = getMealTags(r.getInt("ID"));
                meals.add(new Meal(r.getInt("ID"),r.getString("NAME"),r.getInt("CNT"),r.getDate("LAST_EATEN"),
                    r.getBoolean("FAVORITE"),r.getString("RECIPE_URL"),r.getString("NOTES"),ingredients,tags));
            }
            return meals;
        });
    }

    public List<Week> getAllWeeks() {
        return jdbcTemplate.query(queryStore.get("getAllWeeks"), r -> {
            List<Week> weeks = new ArrayList<>();
			while (r.next()) {
                List<Meal> meals = getMeelsInWeek(r.getInt("ID"));
                weeks.add(new Week(r.getInt("ID"),r.getDate("DATE_BEGIN"),r.getDate("DATE_END"),meals));
			}
            return weeks;
		});
    }

    public Map<Integer,String> getAllTags() {
        return jdbcTemplate.query(queryStore.get("getAllTags"), r -> {
            Map<Integer,String> tags = new HashMap<>();
            while (r.next())
                tags.put(r.getInt("ID"),r.getString("NAME"));
            return tags;
        });
    }

    public List<Ingredient> getAllIngredients() {
      return jdbcTemplate.query(queryStore.get("getAllIngredients"), r -> {
          List<Ingredient> ingredients = new ArrayList<>();
          while (r.next()) {
              ingredients.add(new Ingredient(r.getInt("ID"),r.getString("NAME")));
          }
          return ingredients;
      });
    }

    public Meal getMealById(int id) {
        return jdbcTemplate.query(queryStore.get("getMealById"), new Object[] {id}, r -> {
            r.next();
            return buildMealFromResultSet(r);
        });
    }

    public Week getWeekById(int id) {
        return jdbcTemplate.query(queryStore.get("getWeekById"), new Object[] {id}, r -> {
            r.next();
            List<Meal> meals = getMeelsInWeek(r.getInt("ID"));
            return new Week(r.getInt("ID"),r.getDate("DATE_BEGIN"),r.getDate("DATE_END"),meals);
        });
    }

    private List<Meal> getMeelsInWeek(int weekId) {
        return jdbcTemplate.query(queryStore.get("getAllMealsInWeek"), new Object[] {weekId}, r -> {
            List<Meal> meals = new ArrayList<>();
            while (r.next()){
                meals.add(buildMealFromResultSet(r));
            }
            return meals;
        });
    }

    private Meal buildMealFromResultSet(ResultSet rs) throws SQLException {
        List<Ingredient> ingredients = getIngredientsInMeal(rs.getInt("ID"));
        Date lastEaten = getMealLastEaten(rs.getInt("ID"));
        Map<Integer,String> tags = getMealTags(rs.getInt("ID"));
        return new Meal(rs.getInt("ID"),rs.getString("NAME"),0,lastEaten,
            false,rs.getString("RECIPE_URL"),rs.getString("NOTES"),ingredients,tags);
    }

    private Map<Integer, String> getMealTags(int mealId) {
        return jdbcTemplate.query(queryStore.get("getMealTags"), new Object[] {mealId}, r -> {
            Map<Integer,String> tags = new HashMap<>();
            while (r.next())
                tags.put(r.getInt("TAG_ID"),r.getString("NAME"));
            return tags;
        });
    }

    private Date getMealLastEaten(int mealId){
        return jdbcTemplate.query(queryStore.get("getMealLastEaten"), new Object[] {mealId}, r -> {
            if (r.next())
                return r.getDate("LAST_EATEN");
            else return null;
        });
    }

    private List<Ingredient> getIngredientsInMeal(int mealId) {
        return jdbcTemplate.query(queryStore.get("getIngredientsInMeal"), new Object[] {mealId}, r -> {
            List<Ingredient> ingredients = new ArrayList<>();
            while (r.next())
                ingredients.add(new Ingredient(r.getInt("ID"),r.getString("NAME")));
            return ingredients;
        });
    }

}
