Feature: Backend demo

  Background:
    Given we know about these cities:
      | name      | province |
      | Toronto   | Ontario  |
      | Montréal  | Québec   |
      | St-Armand | Québec   |

  Scenario: Adding a city
    Given we are logged in
    When we add a new city:
      | name    | province |
      | St-Jean | Québec   |
    And the new city is added
    And we ask for the list of cities
    Then we get:
      | name      | province |
      | Montréal  | Québec   |
      | St-Armand | Québec   |
      | St-Jean   | Québec   |
      | Toronto   | Ontario  |

  Scenario: Deleting a city
    Given we are logged in
    When we delete Toronto
    And the city is deleted
    And we ask for the list of cities
    Then we get:
      | name      | province |
      | Montréal  | Québec   |
      | St-Armand | Québec   |

  Scenario Outline: Get city details [<name>]
    Given we are logged in
    When we ask for <name>'s details
    Then we get the city details:
      | name     | <name>     |
      | province | <province> |
    Examples:
      | name      | province |
      | Montréal  | Québec   |
      | St-Armand | Québec   |
      | Toronto   | Ontario  |

  Scenario: Get cities list
    Given we are logged in
    When we ask for the list of cities
    Then we get:
      | name      | province |
      | Montréal  | Québec   |
      | St-Armand | Québec   |
      | Toronto   | Ontario  |

  Scenario: Updating a city
    Given we are logged in
    When we change the name of Montréal to Ville-Marie
    And the city is updated
    And we ask for the list of cities
    Then we get:
      | name        | province |
      | St-Armand   | Québec   |
      | Toronto     | Ontario  |
      | Ville-Marie | Québec   |

  Scenario: Adding a city - ERROR - duplicate
    Given we are logged in
    When we add a new city:
      | name    | province |
      | Toronto | Ontario  |
    Then we should get a CONFLICT (409) error

  Scenario: Updating a city - ERROR - duplicate
    Given we are logged in
    When we change the name of St-Armand to Montréal
    Then we should get a CONFLICT (409) error

  Scenario Outline: <operation> - ERROR - input error - <request>

  We won't be testing the get single with an empty name, since
  that would be the same request as the get all cities.

    Given we are logged in
    When we <request>
    Then we should get a BAD_REQUEST (400) error
    Examples:
      | operation       | request                          |
      | Adding a city   | add a new city with a blank name |
      | Deleting a city | delete an empty city             |
      | Updating a city | blank the name of Toronto        |
      | Updating a city | update an empty city             |

  Scenario Outline: <operation> - ERROR - not logged in
    Given we are not logged in
    When we <request>
    Then we should get a UNAUTHORIZED (401) error
    Examples:
      | operation        | request                    |
      | Adding a city    | add a new city             |
      | Deleting a city  | delete Toronto             |
      | Get city details | ask for Toronto's details  |
      | Get cities list  | ask for the list of cities |
      | Updating a city  | update Toronto             |

  Scenario Outline: <operation> - ERROR - unknown city
    Given we are logged in
    When we <request>
    Then we should get a NOT_FOUND (404) error
    Examples:
      | operation        | request                  |
      | Deleting a city  | delete Bombay            |
      | Get city details | ask for Bombay's details |
      | Updating a city  | update Bombay            |
