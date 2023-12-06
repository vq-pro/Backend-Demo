Feature: Kata - Get cities with riders

#  Background:
#    Given we know about these wheels:
#      | brand    | name    |
#      | Inmotion | V8F     |
#      | KingSong | S18     |
#      | KingSong | 14D     |
#      | Veteran  | Sherman |

#  Scenario: Get cities with riders
#    Given we are logged in
#    And we know about these riders:
#      | name      | wheel   | city      |
#      | Mia       | V8F     | Montreal  |
#      | Patrick   | Sherman | St-Armand |
#      | Elsa      | 14D     | St-Armand |
#      | Jerome    | Sherman | Longueuil |
#      | Evens     | Sherman | St-Armand |
#      | Charlotte | V8F     | Montreal  |
#    When we ask for the list of cities with 2 or more riders
#    Then we get these cities:
#      | city      | wheels                               |
#      | St-Armand | Sherman (Evens, Patrick), 14D (Elsa) |
#      | Montreal  | V8F (Charlotte, Mia)                 |
