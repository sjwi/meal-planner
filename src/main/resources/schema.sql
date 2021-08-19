DROP TABLE IF EXISTS Meals;
CREATE TABLE Meals (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(255) NOT NULL,
  FAVORITE BOOLEAN DEFAULT 0,
  RECIPE_URL varchar(2055),
  NOTES varchar(2055),
  DISABLED BOOLEAN DEFAULT 0,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO Meals (NAME, FAVORITE) values 
('Meal 1',0),
('Meal 2',0),
('Meal 3',1),
('Meal 4',0),
('Meal 5',0),
('Meal 6',0),
('Meal 7',0),
('Meal 8',1),
('Meal 9',0),
('Meal 10',0),
('Meal 11',0),
('Meal 12',0),
('Meal 13',0);
INSERT INTO Meals (NAME, FAVORITE, RECIPE_URL, NOTES) values 
('Meal 14',0,'https://stephenky.com','Notes about this meal'),
('Meal 15',0,'https://stephenky.com','Notes about this meal'),
('Meal 16',0,'https://stephenky.com','Notes about this meal'),
('Meal 17',0,'https://stephenky.com','Notes about this meal'),
('Meal 18',0,'https://stephenky.com','Notes about this meal'),
('Meal 19',0,'https://stephenky.com','Notes about this meal'),
('Meal 20',0,'https://stephenky.com','Notes about this meal'),
('Meal 21',0,'https://stephenky.com','Notes about this meal'),
('Meal 22',0,'https://stephenky.com','Notes about this meal');

DROP TABLE IF EXISTS Ingredients;
CREATE TABLE Ingredients (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(255),
  DISABLED BOOLEAN DEFAULT 0,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO Ingredients (NAME) values
('Ingredient 1'),
('Ingredient 2'),
('Ingredient 3'),
('Ingredient 4'),
('Ingredient 5'),
('Ingredient 6'),
('Ingredient 7'),
('Ingredient 8'),
('Ingredient 9'),
('Ingredient 10');


DROP TABLE IF EXISTS MealIngredients;
CREATE TABLE MealIngredients (
  ID int(11) NOT NULL AUTO_INCREMENT,
  MEAL_ID int(11),
  INGREDIENT_ID int(11),
  DISABLED BOOLEAN DEFAULT 0,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO MealIngredients (MEAL_ID,INGREDIENT_ID) values
(1,1),
(1,5),
(1,9),
(2,2),
(2,6),
(3,10),
(3,3),
(3,7),
(3,2),
(3,4),
(3,5),
(4,4),
(4,4),
(4,8),
(4,9),
(5,2),
(6,3),
(7,3),
(7,4),
(8,4),
(9,5),
(9,4);

DROP TABLE IF EXISTS WeeklySchedule;
CREATE TABLE WeeklySchedule (
  ID int(11) NOT NULL AUTO_INCREMENT,
  DATE_BEGIN date,
  DATE_END date,
  DISABLED BOOLEAN DEFAULT 0,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO WeeklySchedule (DATE_BEGIN,DATE_END) values
(PARSEDATETIME('08/06/2021','MM/dd/yyyy'), PARSEDATETIME('08/13/2021','MM/dd/yyyy')),
(PARSEDATETIME('07/30/2021','MM/dd/yyyy'), PARSEDATETIME('08/06/2021','MM/dd/yyyy')),
(PARSEDATETIME('07/23/2021','MM/dd/yyyy'), PARSEDATETIME('07/30/2021','MM/dd/yyyy')),
(PARSEDATETIME('07/16/2021','MM/dd/yyyy'), PARSEDATETIME('07/23/2021','MM/dd/yyyy')),
(PARSEDATETIME('07/09/2021','MM/dd/yyyy'), PARSEDATETIME('07/16/2021','MM/dd/yyyy')),
(PARSEDATETIME('07/02/2021','MM/dd/yyyy'), PARSEDATETIME('07/09/2021','MM/dd/yyyy')),
(PARSEDATETIME('06/25/2021','MM/dd/yyyy'), PARSEDATETIME('07/02/2021','MM/dd/yyyy'));
-- (DATEADD(DAY, -4, CURRENT_DATE), DATEADD(DAY, 3, CURRENT_DATE)),
-- (DATEADD(DAY, 4, CURRENT_DATE), DATEADD(DAY, 11, CURRENT_DATE));


DROP TABLE IF EXISTS PlannedMeals;
CREATE TABLE PlannedMeals (
  ID int(11) NOT NULL AUTO_INCREMENT,
  SCHEDULE_ID int(11),
  MEAL_ID int(11),
  DISABLED BOOLEAN DEFAULT 0,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO PlannedMeals (SCHEDULE_ID,MEAL_ID) values
(1,1),
(1,3),
(1,5),
(1,7),
(2,2),
(2,1),
(3,3),
(3,6),
(3,8),
(3,9),
(3,10),
(4,11),
(4,12),
(4,13),
(4,14),
(5,15),
(5,16),
(5,17),
(5,18);

DROP TABLE IF EXISTS Tags;
CREATE TABLE Tags (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(255),
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO Tags (NAME) values
('Tag 1'),
('Tag 2'),
('Tag 3'),
('Tag 4'),
('Tag 5');

DROP TABLE IF EXISTS MealTags;
CREATE TABLE MealTags (
  ID int(11) NOT NULL AUTO_INCREMENT,
  TAG_ID int(11),
  MEAL_ID int(11),
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO MealTags (TAG_ID,MEAL_ID) values
(1,1),
(1,3),
(1,5),
(2,1),
(2,4),
(2,6),
(2,7),
(2,8),
(2,9),
(2,2),
(3,3),
(3,8),
(3,7),
(3,5),
(3,9),
(4,8),
(4,1),
(4,3),
(4,2),
(4,7),
(4,6),
(5,1),
(5,2),
(5,3),
(5,4),
(5,5),
(5,8),
(5,9);