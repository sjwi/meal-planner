DROP TABLE IF EXISTS Meals;
CREATE TABLE Meals (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(255) NOT NULL,
  FAVORITE BOOLEAN DEFAULT 0,
  RECIPE_URL varchar(2055),
  NOTES varchar(2055),
  USER varchar(50) NOT NULL,
  DISABLED BOOLEAN DEFAULT 0,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO Meals (NAME, FAVORITE, USER) values 
('Meal 1',1,'de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 2',0,'de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 3',1,'de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 4',0,'de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 5',0,'de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 6',0,'de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 7',0,'de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 8',1,'de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 9',0,'de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 10',0,'de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 11',0,'de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 12',0,'de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 13',0,'de9a8c23-bb74-512d-a390-2b8cb659ebcf');
INSERT INTO Meals (NAME, FAVORITE, RECIPE_URL, NOTES,USER) values 
('Meal 23',1,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 24',1,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 25',0,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 26',0,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 27',1,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 28',0,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 29',1,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 30',0,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 31',0,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf');

INSERT INTO Meals (NAME, FAVORITE, RECIPE_URL, NOTES,USER) values 
('Meal 14',0,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 15',0,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 16',0,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 17',0,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 18',0,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 19',0,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 20',0,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 21',0,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Meal 22',0,'https://stephenky.com','Notes about this meal','de9a8c23-bb74-512d-a390-2b8cb659ebcf');

DROP TABLE IF EXISTS Ingredients;
CREATE TABLE Ingredients (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(255),
  USER varchar(50) NOT NULL,
  DISABLED BOOLEAN DEFAULT 0,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO Ingredients (NAME, USER) values
('Ingredient 1','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Ingredient 2','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Ingredient 3','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Ingredient 4','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Ingredient 5','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Ingredient 6','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Ingredient 7','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Ingredient 8','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Ingredient 9','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Ingredient 10','de9a8c23-bb74-512d-a390-2b8cb659ebcf');

DROP TABLE IF EXISTS Sides;
CREATE TABLE Sides (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(255),
  RECIPE_URL varchar(2055),
  NOTES varchar(2055),
  USER varchar(50) NOT NULL,
  DISABLED BOOLEAN DEFAULT 0,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO Sides (NAME, USER) values
('Side 1','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 2','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 3','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 4','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 5','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 6','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 7','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 8','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 9','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 10','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 11','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 12','de9a8c23-bb74-512d-a390-2b8cb659ebcf');

INSERT INTO Sides (NAME,RECIPE_URL,NOTES,USER) values
('Side 13','https://stephenky.com','This is a side note','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 14','https://stephenky.com','This is a side note','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 15','https://stephenky.com','This is a side note','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 16','https://stephenky.com','This is a side note','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 17','https://stephenky.com','This is a side note','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 18','https://stephenky.com','This is a side note','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 19','https://stephenky.com','This is a side note','de9a8c23-bb74-512d-a390-2b8cb659ebcf'),
('Side 20','https://stephenky.com','This is a side note','de9a8c23-bb74-512d-a390-2b8cb659ebcf');

DROP TABLE IF EXISTS MealSides;
CREATE TABLE MealSides (
  ID int(11) NOT NULL AUTO_INCREMENT,
  MEAL_ID int(11),
  SIDE_ID int(11),
  DISABLED BOOLEAN DEFAULT 0,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO MealSides(MEAL_ID,SIDE_ID) values
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
(9,4),
(10,12),
(11,10),
(12,11),
(13,3),
(13,4),
(14,2),
(15,8),
(15,4);

DROP TABLE IF EXISTS SideIngredients;
CREATE TABLE SideIngredients (
  ID int(11) NOT NULL AUTO_INCREMENT,
  SIDE_ID int(11),
  INGREDIENT_ID int(11),
  DISABLED BOOLEAN DEFAULT 0,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO SideIngredients (SIDE_ID,INGREDIENT_ID) values
(1,1),
(2,5),
(2,9),
(2,2),
(4,10),
(5,3),
(6,7),
(7,2),
(8,4),
(9,5),
(10,4),
(11,4),
(12,8),
(13,3),
(13,4),
(14,2),
(15,8),
(15,4);

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
  USER varchar(50) NOT NULL DEFAULT 'de9a8c23-bb74-512d-a390-2b8cb659ebcf',
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
  USER varchar(50) NOT NULL DEFAULT 'de9a8c23-bb74-512d-a390-2b8cb659ebcf',
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

DROP TABLE IF EXISTS users;
CREATE TABLE users (
  username varchar(50) NOT NULL,
  firstname varchar(50),
  lastname varchar(50),
  refreshToken varchar(512),
  email varchar(255),
  sort varchar(50) DEFAULT 'NAME',
  sortDirection varchar(50) DEFAULT 'ASC',
  pinFavorites BOOLEAN DEFAULT 0,
  enabled tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS authorities;
CREATE TABLE authorities (
  username varchar(50) NOT NULL,
  authority varchar(50) NOT NULL,
  UNIQUE KEY ix_auth_username (username,authority),
  CONSTRAINT authorities_ibfk_1 FOREIGN KEY (username) REFERENCES users (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS StoredLogins;
CREATE TABLE StoredLogins (
  ID int(11) NOT NULL AUTO_INCREMENT,
  username varchar(50) NOT NULL,
  LOGIN_COOKIE varchar(5000) NOT NULL,
  CREATED_ON datetime NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO StoredLogins (username,LOGIN_COOKIE,CREATED_ON)
values
('de9a8c23-bb74-512d-a390-2b8cb659ebcf','UvH8iML60+LYJBtU8JArpj6+S6jJG2XVBHPGKIRs+B9G+xWQqt3+Jy9mw4Hpj54Jzqs/X/bNJS92hDiTNMCErklw1OnU9lA7LF2YMOFc4hx21g0EM7IYTK6ZMjOjBV5LDjkEAnqKVi9VGQ07R1hV9wRXtGLFv2gtTvodaK89vfhCIBu/0AFPQY0mUQz4KzgCeeGg/50YUaH1bCMNzdnAOWjBaqVaQvJ+h8EsX6j9ZT6uNbM3TEZGgsaoRNAecolcF4XWs4szjcPMcZjj8GCTDlZGd/0NB0E2pVqK6hhYE8y/0IzDYbkJc3CR9jCb+FVd2FmBoXA89b3JHoWhfUck4w==','2019-08-23 17:40:55');
