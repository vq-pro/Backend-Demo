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

  @Ignore
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

  @Ignore
  Scenario: Updating a wheel
    Given we are logged in
    When we change the Sherman's name to Super Sherman
    And we ask for the list of wheels
    Then we get:
      | brand    | name          |
      | KingSong | S18           |
      | Veteran  | Super Sherman |

  @Ignore
  Scenario: Adding a wheel - ERROR - duplicate
    Given we are logged in
    When we add a new wheel:
      | brand             | name    |
      | LeaperKim Veteran | Sherman |
    Then we should get a 409 error

  @Ignore
  Scenario: Adding a wheel - ERROR - null name
    Given we are logged in
    When we add a new wheel:
      | brand    | name |
      | Inmotion |      |
    Then we should get a 400 error

  Scenario Outline: <operation> - ERROR - not logged in
    Given we are not logged in
    When we <request>
    Then we should get a 401 error
    Examples:
      | operation              | request                       |
#      | Adding a wheel         | add a new wheel               |
#      | Deleting a wheel       | delete the Sherman            |
      | Get all wheels details | ask for the list of wheels    |
      | Get wheel details      | ask for the Sherman's details |
#      | Updating a wheel       | change the Sherman's name     |

  Scenario Outline: <operation> - ERROR - unknown wheel
    Given we are logged in
    When we <request>
    Then we should get a 404 error
    Examples:
      | operation         | request                      |
#      | Deleting a wheel  | delete the Segway            |
      | Get wheel details | ask for the Segway's details |
#      | Updating a wheel  | change the Segway's name     |

#  Scenario Outline: Calculate battery percentage [<wheel> @ <voltage>]
#    Given we are logged in
#    When we ask for the percentage for <voltage> on the <wheel>
#    Then we get <percentage>
#    Examples:
#      | wheel   | voltage | percentage |
#      | Sherman | 75.6V   | 0.0%       |
##      | Sherman | 92.5V   | 67.1%      |
##      | S18     | 72.0V   | 50.0%       |

#  Scenario Outline: Get wheel details [<wheel>]
#    Given we are logged in
#    When we ask for the <wheel>'s details
#    Then we get the wheel details:
#      | Brand       | <brand>       |
#      | Name        | <wheel>       |
#      | Voltage Max | <voltage max> |
#      | Voltage Min | <voltage min> |
#    Examples:
#      | wheel   | brand    | voltage min | voltage max |
#      | Sherman | Veteran  | 75.6V       | 100.8V      |
#      | S18     | KingSong | 60.0V       | 84.0V       |

#  Background:
#    Given we know about these wheels:
#      | brand    | name    | voltage min | voltage max |
#      | KingSong | S18     | 60.0V       | 84.0V       |
#      | Veteran  | Sherman | 75.6V       | 100.8V      |

#  Scenario Outline: <method> - ERROR - not logged in

#  Scenario Outline: <method> - ERROR - unknown wheel
