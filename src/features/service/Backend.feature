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

  Scenario: Get all wheels details
    Given we are logged in
    When we ask for the list of wheels
    Then we get:
      | brand    | name    |
      | KingSong | S18     |
      | Veteran  | Sherman |

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

  @Ignore
  Scenario Outline: <operation> - ERROR - input error - <request>
    Given we are logged in
    When we <request>
    Then we should get a <status> (<code>) error
    Examples:
      | operation      | request                           | status      | code |
      | Adding a wheel | add a new wheel with a blank name | BAD_REQUEST | 400  |
#      | Deleting a wheel | delete an empty wheel             | METHOD_NOT_ALLOWED | 405  |
#      | Updating a wheel | update an empty wheel             | METHOD_NOT_ALLOWED | 405  |
#      | Updating a wheel | blank the Sherman's name          | BAD_REQUEST        | 400  |

  Scenario Outline: <operation> - ERROR - not logged in
    Given we are not logged in
    When we <request>
    Then we should get a UNAUTHORIZED (401) error
    Examples:
      | operation              | request                       |
      | Adding a wheel         | add a new wheel               |
      | Deleting a wheel       | delete the Sherman            |
      | Get all wheels details | ask for the list of wheels    |
      | Get wheel details      | ask for the Sherman's details |
      | Updating a wheel       | change the Sherman's name     |

  Scenario Outline: <operation> - ERROR - unknown wheel
    Given we are logged in
    When we <request>
    Then we should get a NOT_FOUND (404) error
    Examples:
      | operation         | request                      |
      | Deleting a wheel  | delete the Segway            |
      | Get wheel details | ask for the Segway's details |
      | Updating a wheel  | change the Segway's name     |

#  Scenario Outline: Calculate battery percentage [<wheel> @ <voltage>]
#    Given we are logged in
#    When we ask for the percentage for <voltage> on the <wheel>
#    Then we get <percentage>
#    Examples:
#      | wheel   | voltage | percentage |
#      | Sherman | 75.6V   | 0.0%       |
##      | Sherman | 92.5V   | 67.1%      |
##      | S18     | 73.0V   | 54.2%      |

#  Scenario Outline: Get wheel details [<name>]
#    Given we are logged in
#    When we ask for the <name>'s details
#    Then we get the wheel details:
#      | brand       | <brand>       |
#      | name        | <name>        |
#      | voltage max | <voltage max> |
#      | voltage min | <voltage min> |
#    Examples:
#      | brand    | name    | voltage min | voltage max |
#      | Veteran  | Sherman | 75.6V       | 100.8V      |
#      | KingSong | S18     | 60.0V       | 84.0V       |

#  Background:
#    Given we know about these wheels:
#      | brand    | name    | voltage min | voltage max |
#      | KingSong | S18     | 60.0V       | 84.0V       |
#      | Veteran  | Sherman | 75.6V       | 100.8V      |

#  Scenario Outline: <method> - ERROR - input error
#      | Adding a wheel               | add a new wheel with 0V voltage              | BAD_REQUEST        | 400  |
#      | Calculate battery percentage | ask for the percentage on an empty wheel     | BAD_REQUEST        | 400  |
#      | Calculate battery percentage | ask for the percentage for 0V on the Sherman | BAD_REQUEST        | 400  |

#  Scenario Outline: <method> - ERROR - not logged in

#  Scenario Outline: <method> - ERROR - unknown wheel
