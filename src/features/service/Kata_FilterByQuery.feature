Feature: Kata - Filter By Query

#  Scenario Outline: Get cities by population [<max_population>]
#    Given we are logged in
#    When we ask for the list of cities with at most <max_population> people
#    Then we get these cities: <result>
#    Examples:
#      | max_population | result                                                |
#      | 0              |                                                       |
#      | 1230           | St-Armand (1228)                                      |
#      | 1800000        | St-Armand (1228)                                      |
#      | 1899000        | Montréal (1.875M), St-Armand (1228)                   |
#      | 2800000        | Toronto (2.794M), Montréal (1.875M), St-Armand (1228) |
#
#  Scenario Outline: Get city details [<name>]
#    Given we are logged in
#    When we ask for <name>'s details
#    Then we get the city details:
#      | name       | <name>       |
#      | population | <population> |
#      | province   | <province>   |
#    Examples:
#      | name      | population | province |
#      | Montréal  | 1.875M     | Québec   |
#      | St-Armand | 1228       | Québec   |
#      | Toronto   | 2.794M     | Ontario  |
#
#  Background:
#    Given we know about these cities:
#      | name      | population | province |
#      | Toronto   | 2.794M     | Ontario  |
#      | Montréal  | 1.875M     | Québec   |
#      | St-Armand | 1228       | Québec   |

#  Scenario: Get cities list
#    Given we are logged in
#    When we ask for the list of cities
#    Then we get:
#      | name      | population | province |
#      | Montréal  | 1.875M     | Québec   |
#      | St-Armand | 1228       | Québec   |
#      | Toronto   | 2.794M     | Ontario  |
#
#  Scenario Outline: <method> - ERROR - input error
#  | Adding a city        | add a new city with a population of 0|
#  | Get cities by population | ask for the list of cities with at most -1 people |
#
#  Scenario Outline: <method> - ERROR - not logged in
#  | Get cities by population | ask for the list of cities with at most 0 people |
