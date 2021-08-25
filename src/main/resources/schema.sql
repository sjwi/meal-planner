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
('Meal 1',1,'admin'),
('Meal 2',0,'admin'),
('Meal 3',1,'admin'),
('Meal 4',0,'admin'),
('Meal 5',0,'admin'),
('Meal 6',0,'admin'),
('Meal 7',0,'admin'),
('Meal 8',1,'admin'),
('Meal 9',0,'admin'),
('Meal 10',0,'admin'),
('Meal 11',0,'admin'),
('Meal 12',0,'admin'),
('Meal 13',0,'admin');
INSERT INTO Meals (NAME, FAVORITE, RECIPE_URL, NOTES,USER) values 
('Meal 14',0,'https://stephenky.com','Notes about this meal','USER'),
('Meal 15',0,'https://stephenky.com','Notes about this meal','USER'),
('Meal 16',0,'https://stephenky.com','Notes about this meal','USER'),
('Meal 17',0,'https://stephenky.com','Notes about this meal','USER'),
('Meal 18',0,'https://stephenky.com','Notes about this meal','USER'),
('Meal 19',0,'https://stephenky.com','Notes about this meal','USER'),
('Meal 20',0,'https://stephenky.com','Notes about this meal','USER'),
('Meal 21',0,'https://stephenky.com','Notes about this meal','USER'),
('Meal 22',0,'https://stephenky.com','Notes about this meal','USER');

DROP TABLE IF EXISTS Ingredients;
CREATE TABLE Ingredients (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(255),
  USER varchar(50) NOT NULL,
  DISABLED BOOLEAN DEFAULT 0,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO Ingredients (NAME, USER) values
('Ingredient 1','admin'),
('Ingredient 2','admin'),
('Ingredient 3','admin'),
('Ingredient 4','admin'),
('Ingredient 5','admin'),
('Ingredient 6','admin'),
('Ingredient 7','admin'),
('Ingredient 8','admin'),
('Ingredient 9','admin'),
('Ingredient 10','admin');

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
('Side 1','admin'),
('Side 2','admin'),
('Side 3','admin'),
('Side 4','admin'),
('Side 5','admin'),
('Side 6','admin'),
('Side 7','admin'),
('Side 8','admin'),
('Side 9','admin'),
('Side 10','admin'),
('Side 11','admin'),
('Side 12','admin');

INSERT INTO Sides (NAME,RECIPE_URL,NOTES,USER) values
('Side 13','stephenky.com','This is a side note','admin'),
('Side 14','stephenky.com','This is a side note','admin'),
('Side 15','stephenky.com','This is a side note','admin'),
('Side 16','stephenky.com','This is a side note','admin'),
('Side 17','stephenky.com','This is a side note','admin'),
('Side 18','stephenky.com','This is a side note','admin'),
('Side 19','stephenky.com','This is a side note','admin'),
('Side 20','stephenky.com','This is a side note','admin');

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
  USER varchar(50) NOT NULL DEFAULT 'admin',
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
  USER varchar(50) NOT NULL DEFAULT 'admin',
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
  firstname varchar(50) NOT NULL,
  lastname varchar(50) NOT NULL,
  password varchar(100) NOT NULL,
  email varchar(255),
  sort varchar(50) DEFAULT 'NAME',
  sortDirection varchar(50) DEFAULT 'ASC',
  pinFavorites BOOLEAN DEFAULT 0,
  enabled tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO users (username,firstname,lastname,password,email,enabled,pinFavorites,sort,sortDirection) VALUES 
('admin','Demo','User','$2a$10$KnRdXb09WIgf1gYwYAj/pO7mB7Rp0i0xejpncp2ZZnlqZW9sj4h/m','stephenjw@fastmail.com',1,1,'CNT','DESC');

DROP TABLE IF EXISTS authorities;
CREATE TABLE authorities (
  username varchar(50) NOT NULL,
  authority varchar(50) NOT NULL,
  UNIQUE KEY ix_auth_username (username,authority),
  CONSTRAINT authorities_ibfk_1 FOREIGN KEY (username) REFERENCES users (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO authorities VALUES 
('admin','USER'),
('admin','ADMIN');

DROP TABLE IF EXISTS StoredLogins;
CREATE TABLE StoredLogins (
  ID int(11) NOT NULL AUTO_INCREMENT,
  username varchar(50) NOT NULL,
  LOGIN_COOKIE varchar(5000) NOT NULL,
  CREATED_ON datetime NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO StoredLogins (username,LOGIN_COOKIE,CREATED_ON) values
('admin','DeT5FlpnmstPnU3O+A3omeHRYXceastOig+wRxEB/gyHKGe1oymRVfxnt3j5xX64N4+J5TybrD0+eFh+8qGdEcVzycrDsuFMQwtOi7+gk1R396O8Fujx8SbzOgmcAq5Fj+FG0UiYeH/dJCv6wM16qIbvH4oZdEwG49XnRSSpSm2EikOi6366kY2j+2w7Td4apfjaepc+GX3rx4AB7mTKEqdQR+MEuh4Rs6d90DB9nIHa7dA2jVt7ay6LHosz3HhTAwkz8i5RWWkFO+uZDuQU8wY8fqs6MhriW+Y8/c2sZizpR0m/ifO68BLJgPdOv4zxaZU3Qze3JXT8l5AUlUZOTw==', CURRENT_TIMESTAMP);