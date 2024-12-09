Feature: Kata - Get cities with people

#  Scenario: Get cities with people
#    Given we are logged in
#    And we know about these people:
#      | name      | city      |
#      | Elsa      | St-Armand |
#      | Mia       | Montréal  |
#      | Brett     | Toronto   |
#      | Jerome    | Longueuil |
#      | Patrick   | St-Armand |
#      | Brian     | Toronto   |
#      | Charlotte | Montréal  |
#      | Evens     | St-Armand |
#    When we ask for the list of cities with 2 or more people
#    Then we get these cities:
#      | city      | people               |
#      | St-Armand | Elsa, Evens, Patrick |
#      | Montréal  | Charlotte, Mia       |
#      | Toronto   | Brett, Brian         |
#
#  Background:
#    Given we know about these cities:
#      | name      | province |
#      | Toronto   | Ontario  |
#      | Longueuil | Québec   |
#      | Montréal  | Québec   |
#      | St-Armand | Québec   |
#
#  Scenario Outline: <method> - ERROR - input error
#| Get cities with people | ask for the list of cities with -1 or more people |

#  Scenario Outline: <method> - ERROR - not logged in
#| Get cities with people | ask for the list of cities with 0 or more people |

#  CREATE TABLE cities
#  (
#  id    SERIAL PRIMARY KEY,
#  name     VARCHAR(45) NOT NULL,
#  province VARCHAR(45) NOT NULL,
#
#  UNIQUE (name)
#  );
#
#  CREATE TABLE people
#  (
#  id         SERIAL PRIMARY KEY,
#  name    VARCHAR(45) NOT NULL,
#  city_id SERIAL      NOT NULL,
#
#  UNIQUE (name)
#  );
#
#  value = "SELECT * FROM ("
#  + "SELECT *,"
#  + " (SELECT COUNT(*) FROM people pp WHERE p.city_id = pp.city_id) AS nbPeopleInCity,"
#  + " (SELECT name FROM cities cc WHERE p.city_id = cc.id) AS cityName"
#  + " FROM people p"
#  + ") AS combined "
#  + "WHERE nbPeopleInCity >= :minPeople "
#  + "ORDER BY "
#  + " nbPeopleInCity DESC,"
#  + " cityName,"
#  + " name"
