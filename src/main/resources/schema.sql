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
('Frozen Pizza & Salad',0),
('Creamed Corn Chicken',0),
('Mushroom penne with walnut pest',0),
('Lemon chicken orzo soup',0),
('Burgers',0),
('Orange chicken',0),
('Chicken tikka masala',0),
('Tortellini with turkey meatballs',0),
('Smoked sausage and potatoes',0),
('BBQ chicken sandwiches',0),
('Beef tacos',0),
('Thai peanut chicken with rice noodles',0),
('Breakfast burritos',0),
('French dip sandwiches',0),
('Chicken tortellini soup',0),
('Grilled filet and chicken',0),
('Spaghetti with turkey meatballs',0),
('Chicken tinga bowls',0),
('Cookout burgers and dogs',0),
('Chili',0),
('Pork tenderloin and polenta',0),
('Chicken tacos',0),
('Chicken strips and potatoes',0),
('Chicken and dumplings',0),
('White chicken chili',0),
('Chicken tenders with red potatoes',0),
('Date night rigatoni ',0),
('BBQ chicken baked potato bar',0),
('Egg sandwiches and tots',0),
('Korean tacos',0),
('Dirty rice with smoked sausage',0),
('Black bean crispy tacos w/ mango salsa',0),
('Teriyaki pork with pineapple',0),
('Spaghetti with meatballs',0),
('Sheet pan chicken and cauliflower rice',0),
('Zuppa toscana ',0),
('Grilled chicken sandwiches',0),
('Panko crusted cod with cheesy lemon orzo',0),
('Ground turkey & veggie lo mein',0),
('Rosemary pork tenderloin',0),
('Baked potatoes, steak, asparagus',0),
('Teriyaki pork with pineapple',0),
('Chicken gnocchi soup',0),
('Grilled cheese and tomato soup',0),
('Breakfast',0),
('Pesto ravioli',0),
('Pot stickers with fried rice',0),
('Jambalaya',0),
('Vodka fettuccine with turkey meatballs',0),
('Whole roast chicken with potatoes',0),
('Mushroom and wild rice soup',0),
('Breakfast hash brown casserole',0),
('Shepherds pie',0),
('Meatloaf with mashed potatoes',0),
('Hot dogs with Mac n cheese',0),
('Sun-dried tomato orzo and chicke',0),
('Turkey sausage and potatoes',0),
('Whole chicken with fries & rings',0),
('Mushroom chicken with egg noodles',0),
('Boursin burgers and potato wedges',0),
('Pan seared chicken with pasta',0),
('Pretzel honey chicken tenders',0),
('Shredded taco chicken bowls',0),
('Alfredo with meatballs & broccoli',0),
('Pancakes and eggs',0),
('Lasagna',0),
('Tilapia with wild rice and asparagus',0),
('Spicy sausage pasta bake',0),
('Taco soup with ground turkey',0),
('Coconut curry chicken',0),
('Tuscan chicken fettuccini',0),
('Charcuterie',0),
('Chicken Tinga Taquitos',0),
('Zucchini Orzo with chicken meatballs',0),
('Tilapia with couscous',0),
('Ravioli soup (sausage)',0),
('Parmesan asparagus orzo and chicken',0),
('Chicken strip salads',0),
('Salmon bowls with mango salsa',0),
('Moroccan chickpea bowls',0),
('Lemon pesto chicken sandwiches',0),
('Monterey chicken spaghetti',0),
('French toast with sausage and tots',0),
('Chicken fajita bowls',0);

DROP TABLE IF EXISTS Ingredients;
CREATE TABLE Ingredients (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(255),
  DISABLED BOOLEAN DEFAULT 0,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS MealIngredients;
CREATE TABLE MealIngredients (
  ID int(11) NOT NULL AUTO_INCREMENT,
  MEAL_ID int(11),
  INGREDIENT_ID int(11),
  DISABLED BOOLEAN DEFAULT 0,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO MealIngredients (MEAL_ID,INGREDIENT_ID) values

DROP TABLE IF EXISTS WeeklySchedule;
CREATE TABLE WeeklySchedule (
  ID int(11) NOT NULL AUTO_INCREMENT,
  DATE_BEGIN date,
  DATE_END date,
  DISABLED BOOLEAN DEFAULT 0,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO WeeklySchedule (DATE_BEGIN,DATE_END) values
('2021-8-06', '2021-8-13'),
('2021-7-30', '2021-8-06'),
('2021-7-23', '2021-7-30'),
('2021-7-16', '2021-7-23'),
('2021-7-09', '2021-7-16'),
('2021-7-02', '2021-7-09'),
('2021-6-25', '2021-7-02'),
('2021-6-18', '2021-6-25'),
('2021-6-11', '2021-6-18'),
('2021-6-04', '2021-6-11'),
('2021-5-28', '2021-6-04'),
('2021-5-21', '2021-5-28'),
('2021-5-14', '2021-5-21'),
('2021-5-07', '2021-5-14'),
('2021-4-30', '2021-5-07'),
('2021-4-23', '2021-4-30'),
('2021-4-16', '2021-4-23'),
('2021-4-09', '2021-4-16'),
('2021-4-02', '2021-4-09'),
('2021-3-26', '2021-4-02'),
('2021-3-19', '2021-3-26'),
('2021-3-12', '2021-3-19'),
('2021-3-05', '2021-3-12'),
('2021-2-26', '2021-3-05'),
('2021-2-19', '2021-2-26'),
('2021-2-12', '2021-2-19'),
('2021-2-05', '2021-2-12'),
('2021-1-29', '2021-2-05'),
('2021-1-22', '2021-1-29'),
('2021-1-15', '2021-1-22'),
('2021-1-08', '2021-1-15'),
('2021-1-01', '2021-1-08'),
('2020-12-25', '2021-1-01'),
('2020-12-18', '2020-12-25'),
('2020-12-11', '2020-12-18'),
('2020-12-04', '2020-12-11'),
('2020-11-27', '2020-12-04'),
('2020-11-20', '2020-11-27'),
('2020-11-13', '2020-11-20'),
('2020-11-06', '2020-11-13'),
('2020-10-30', '2020-11-06'),
('2020-10-23', '2020-10-30'),
('2020-10-16', '2020-10-23'),
('2020-10-09', '2020-10-16'),
('2020-10-02', '2020-10-09'),
('2020-9-25', '2020-10-02'),
('2020-9-18', '2020-9-25'),
('2020-9-11', '2020-9-18'),
('2020-9-04', '2020-9-11'),
('2020-8-28', '2020-9-04'),
('2020-8-21', '2020-8-28'),
('2020-8-14', '2020-8-21'),
('2020-8-07', '2020-8-14'),
('2020-7-31', '2020-8-07'),
('2020-7-24', '2020-7-31'),
('2020-7-17', '2020-7-24'),
('2020-7-10', '2020-7-17'),
('2020-7-03', '2020-7-10'),
('2020-6-26', '2020-7-03'),
('2020-6-19', '2020-6-26'),
('2020-6-12', '2020-6-19'),
('2020-6-05', '2020-6-12'),
('2020-5-29', '2020-6-05'),
('2020-5-22', '2020-5-29'),
('2020-5-15', '2020-5-22');
-- (PARSEDATETIME('07/30/2021','MM/dd/yyyy'), PARSEDATETIME('08/06/2021','MM/dd/yyyy')),
-- (PARSEDATETIME('07/23/2021','MM/dd/yyyy'), PARSEDATETIME('07/30/2021','MM/dd/yyyy')),
-- (PARSEDATETIME('07/16/2021','MM/dd/yyyy'), PARSEDATETIME('07/23/2021','MM/dd/yyyy')),
-- (PARSEDATETIME('07/09/2021','MM/dd/yyyy'), PARSEDATETIME('07/16/2021','MM/dd/yyyy')),
-- (PARSEDATETIME('07/02/2021','MM/dd/yyyy'), PARSEDATETIME('07/09/2021','MM/dd/yyyy')),
-- (PARSEDATETIME('06/25/2021','MM/dd/yyyy'), PARSEDATETIME('07/02/2021','MM/dd/yyyy'));
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

DROP TABLE IF EXISTS Tags;
CREATE TABLE Tags (
  ID int(11) NOT NULL AUTO_INCREMENT,
  NAME varchar(255),
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS MealTags;
CREATE TABLE MealTags (
  ID int(11) NOT NULL AUTO_INCREMENT,
  TAG_ID int(11),
  MEAL_ID int(11),
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

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
('imoodge','Elizabeth','Williams','$2a$10$wcjwEuirYWvhU8hidin7W.2OHuvGoouJFi1ADMF04BEuPAkhupyAq','imoodge@gmail.com',1,0,'NAME','ASC'),
('sjwi','Stephen','Williams','$2a$10$7y499wToK2lGwgbkCMu1F.DKJ0B3iIwEV.yHbEFTP2uiQntOw2h7q','stephenjw@fastmail.com',1,0,'NAME','ASC');

DROP TABLE IF EXISTS authorities;
CREATE TABLE authorities (
  username varchar(50) NOT NULL,
  authority varchar(50) NOT NULL,
  UNIQUE KEY ix_auth_username (username,authority),
  CONSTRAINT authorities_ibfk_1 FOREIGN KEY (username) REFERENCES users (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO authorities VALUES 
('sjwi','USER'),
('sjwi','ADMIN'),
('imoodge','USER'),
('imoodge','ADMIN');

DROP TABLE IF EXISTS StoredLogins;
CREATE TABLE StoredLogins (
  ID int(11) NOT NULL AUTO_INCREMENT,
  username varchar(50) NOT NULL,
  LOGIN_COOKIE varchar(5000) NOT NULL,
  CREATED_ON datetime NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
