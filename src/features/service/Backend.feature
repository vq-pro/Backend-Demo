Feature: Backend demo

  Background:
    Given we know about these wheels:
      | brand    | name    |
      | KingSong | S18     |
      | Veteran  | Sherman |

  Scenario: Adding a wheel
    Given we are logged in
    When we add a new wheel:
      | brand    | name |
      | Inmotion | V14  |
    And the new wheel is added
    And we ask for the list of wheels
    Then we get:
      | brand    | name    |
      | Inmotion | V14     |
      | KingSong | S18     |
      | Veteran  | Sherman |

  Scenario: Deleting a wheel
    Given we are logged in
    When we delete the Sherman
    And the wheel is deleted
    And we ask for the list of wheels
    Then we get:
      | brand    | name |
      | KingSong | S18  |

  Scenario Outline: Get wheel details [<name>]
    Given we are logged in
    When we ask for the <name>'s details
    Then we get the wheel details:
      | brand | <brand> |
      | name  | <name>  |
    Examples:
      | brand    | name    |
      | Veteran  | Sherman |
      | KingSong | S18     |

  Scenario: Get wheels list
    Given we are logged in
    When we ask for the list of wheels
    Then we get:
      | brand    | name    |
      | KingSong | S18     |
      | Veteran  | Sherman |

  Scenario: Updating a wheel
    Given we are logged in
    When we change the Sherman's name to Super Sherman
    And the wheel is updated
    And we ask for the list of wheels
    Then we get:
      | brand    | name          |
      | KingSong | S18           |
      | Veteran  | Super Sherman |

  Scenario: Adding a wheel - ERROR - duplicate
    Given we are logged in
    When we add a new wheel:
      | brand             | name    |
      | LeaperKim Veteran | Sherman |
    Then we should get a CONFLICT (409) error

  Scenario: Updating a wheel - ERROR - duplicate
    Given we are logged in
    When we change the Sherman's name to S18
    Then we should get a CONFLICT (409) error

  Scenario Outline: <operation> - ERROR - input error - <request>
    Given we are logged in
    When we <request>
    Then we should get a BAD_REQUEST (400) error
    Examples:
      | operation        | request                           |
      | Adding a wheel   | add a new wheel with a blank name |
      | Deleting a wheel | delete an empty wheel             |
      | Updating a wheel | update an empty wheel             |
      | Updating a wheel | blank the Sherman's name          |

  Scenario Outline: <operation> - ERROR - not logged in
    Given we are not logged in
    When we <request>
    Then we should get a UNAUTHORIZED (401) error
    Examples:
      | operation         | request                       |
      | Adding a wheel    | add a new wheel               |
      | Deleting a wheel  | delete the Sherman            |
      | Get wheel details | ask for the Sherman's details |
      | Get wheels list   | ask for the list of wheels    |
      | Updating a wheel  | change the Sherman's name     |

  Scenario Outline: <operation> - ERROR - unknown wheel
    Given we are logged in
    When we <request>
    Then we should get a NOT_FOUND (404) error
    Examples:
      | operation         | request                      |
      | Deleting a wheel  | delete the Segway            |
      | Get wheel details | ask for the Segway's details |
      | Updating a wheel  | change the Segway's name     |
