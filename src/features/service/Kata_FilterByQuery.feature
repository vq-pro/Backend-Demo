Feature: Kata - Filter By Query

#  Scenario Outline: Get cities by population [<population>]
#    Given we are logged in
#    When we ask for the list of cities with at most <population> people
#    Then we get these cities: <result>
#    Examples:
#      | population | result                                            |
#      | 0          |                                                   |
#      | 1230       | St-Armand (1228)                                  |
#      | 1800000    | St-Armand (1228)                                  |
#      | 1899000    | Montréal (1.9M), St-Armand (1228)                 |
#      | 2800000    | Toronto (2.8M), Montréal (1.9M), St-Armand (1228) |
#
#  Scenario Outline: Get city details [<name>]
#    Given we are logged in
#    When we ask for <name>'s details
#    Then we get the city details:
#      | name       | <name>       |
#      | province   | <province>   |
#      | population | <population> |
#    Examples:
#      | name      | province | population |
#      | Montréal  | Québec   | 1.875M     |
#      | St-Armand | Québec   | 1228       |
#      | Toronto   | Ontario  | 2.794M     |
#
#  Background:
#    Given we know about these cities:
#      | name      | province | population |
#      | Toronto   | Ontario  | 2.794M     |
#      | Montréal  | Québec   | 1.875M     |
#      | St-Armand | Québec   | 1228       |
#
#  Scenario Outline: <method> - ERROR - input error
#  | Adding a city        | add a new city with a population of 0|
#  | Get cities by population | ask for the list of cities with a battery population of at least -1 |
#
#  Scenario Outline: <method> - ERROR - not logged in
#  | Get cities by population | ask for the list of cities with a battery population of at least 0 |
