Feature: Kata - Traveling between cities

#  Scenario: Traveling between cities
#    Given we are logged in
#    And these distances between cities:
#      | from       | to         | distance |
#      | Montréal   | Lacolle    | 64       |
#      | Montréal   | St-Jean    | 43       |
#      | Pike River | St-Armand  | 13       |
#      | Lacolle    | Venise     | 22       |
#      | St-Jean    | Pike River | 33       |
#      | Venise     | Pike River | 8        |
#    When I go from Montréal to St-Armand
#    Then the trip is calculated to be: Montréal, St-Jean, Pike River, St-Armand
#    And the trip is 89 km

#  Background:
#    Given we know about these cities:
#      | name       | province |
#      | Toronto    | Ontario  |
#      | St-Jean    | Québec   |
#      | Pike River | Québec   |
#      | Lacolle    | Québec   |
#      | Venise     | Québec   |
#      | Montréal   | Québec   |
#      | St-Armand  | Québec   |
