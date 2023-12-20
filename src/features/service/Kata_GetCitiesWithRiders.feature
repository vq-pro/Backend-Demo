Feature: Kata - Get cities with riders

#  Scenario: Get cities with riders
#    Given we are logged in
#    And we know about these riders:
#      | name      | city      | wheel   |
#      | Elsa      | St-Armand | 18XL    |
#      | Mia       | Montreal  | V8F     |
#      | Brett     | Toronto   | RS      |
#      | Elsa      | St-Armand | 14D     |
#      | Jerome    | Longueuil | Sherman |
#      | Patrick   | St-Armand | Sherman |
#      | Brian     | Toronto   | Patton  |
#      | Charlotte | Montreal  | V8F     |
#      | Evens     | St-Armand | Sherman |
#    When we ask for the list of cities with 2 or more riders
#    Then we get these cities:
#      | city      | wheels                                            |
#      | St-Armand | Sherman (Evens, Patrick), 14D (Elsa), 18XL (Elsa) |
#      | Montreal  | V8F (Charlotte, Mia)                              |
#      | Toronto   | Patton (Brian), RS (Brett)                        |

#  Background:
#    Given we know about these wheels:
#      | brand    | name    |
#      | Inmotion | V8F     |
#      | KingSong | 18XL    |
#      | KingSong | S18     |
#      | KingSong | 14D     |
#      | Begode   | RS      |
#      | Veteran  | Patton  |
#      | Veteran  | Sherman |

#  Scenario Outline: <method> - ERROR - input error
#| Get cities with riders | ask for the list of cities with -1 or more riders |

#  Scenario Outline: <method> - ERROR - not logged in
#| Get cities with riders | ask for the list of cities with 0 or more riders |

#  CREATE TABLE riders
#  (
#  id         SERIAL PRIMARY KEY,
#  rider_name VARCHAR(45) NOT NULL,
#  city_name  VARCHAR(45) NOT NULL,
#  wheel_id   SERIAL      NOT NULL,
#
#  UNIQUE (rider_name, city_name)
#  );
#
#  CREATE TABLE wheels
#  (
#  id    SERIAL PRIMARY KEY,
#  brand VARCHAR(45) NOT NULL,
#  name  VARCHAR(45) NOT NULL,
#
#  UNIQUE (name)
#  );
#
#  INSERT INTO WHEELS (ID, brand, NAME) values (1, 'Veteran', 'Sherman');
#  INSERT INTO WHEELS (ID, brand, NAME) values (2, 'Inmotion', 'V8F');
#  INSERT INTO WHEELS (ID, brand, NAME) values (3, 'KingSong', '14D');
#
#  INSERT INTO riders (id, rider_name, city_name, wheel_id) VALUES (1, 'Mia', 'Montreal', 2);
#  INSERT INTO riders (id, rider_name, city_name, wheel_id) VALUES (2, 'Elsa', 'St-Armand', 3);
#  INSERT INTO riders (id, rider_name, city_name, wheel_id) VALUES (3, 'Jerome', 'Longueuil', 1);
#  INSERT INTO riders (id, rider_name, city_name, wheel_id) VALUES (4, 'Patrick', 'St-Armand', 1);
#  INSERT INTO riders (id, rider_name, city_name, wheel_id) VALUES (5, 'Charlotte', 'Montreal', 2);
#  INSERT INTO riders (id, rider_name, city_name, wheel_id) VALUES (6, 'Evens', 'St-Armand', 1);
#
#  SELECT * FROM WHEELS;
#  SELECT * FROM RIDERS;
#
#  SELECT *
#  FROM (
#  SELECT
#  *,
#  (SELECT COUNT(*) FROM riders r WHERE rr.city_name = r.city_name) AS nbRidersInCity,
#  (SELECT COUNT(*) FROM riders r WHERE rr.city_name = r.city_name AND rr.wheel_id = r.wheel_id) AS nbWheelsInCity
#  FROM riders rr
#  ) AS combined
#  WHERE nbRidersInCity >= 2
#  ORDER BY nbRidersInCity DESC,city_name, nbWheelsInCity DESC, rider_name
