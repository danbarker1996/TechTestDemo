Feature: Create and Query Addresses via REST API
  Users should be able to GET the list of addresses, POST new addresses, GET individual addresses and DELETE individual addresses

  Scenario: GET the list of all addresses
    Given the collection of addresses
      | building      | street          | town       | postcode |
      | King's House  | Kings Road West | Newbury    | RG14 5BY |
      | The Malthouse | Elevator Road   | Manchester | M17 1BR  |
      | Holland House | Bury Street     | London     | EC3A 5AW |
    When user asks for all addresses
    Then user receives status code of 200
    And collection of addresses returned
      | building      | street          | town       | postcode |
      | King's House  | Kings Road West | Newbury    | RG14 5BY |
      | The Malthouse | Elevator Road   | Manchester | M17 1BR  |
      | Holland House | Bury Street     | London     | EC3A 5AW |

  Scenario: GET the list of all addresses by postcode
    Given the collection of addresses
      | building      | street          | town       | postcode |
      | King's House  | Kings Road West | Newbury    | RG14 5BY |
      | Queen's House | Kings Road West | Newbury    | RG14 5BY |
      | The Malthouse | Elevator Road   | Manchester | M17 1BR  |
      | Holland House | Bury Street     | London     | EC3A 5AW |
    When user asks for all addresses with postcode "RG14 5BY"
    Then user receives status code of 200
    And collection of addresses returned
      | building      | street          | town    | postcode |
      | King's House  | Kings Road West | Newbury | RG14 5BY |
      | Queen's House | Kings Road West | Newbury | RG14 5BY |

  Scenario: GET an address by ID
    Given the collection of addresses
      | building      | street          | town       | postcode |
      | King's House  | Kings Road West | Newbury    | RG14 5BY |
      | Queen's House | Kings Road West | Newbury    | RG14 5BY |
      | The Malthouse | Elevator Road   | Manchester | M17 1BR  |
      | Holland House | Bury Street     | London     | EC3A 5AW |
    When user asks for the address with the latest ID
    Then user receives status code of 200
    And address returned
      | building      | street      | town   | postcode |
      | Holland House | Bury Street | London | EC3A 5AW |

  Scenario: GET an address by ID that does not exist
    Given the collection of addresses
      | building      | street          | town       | postcode |
      | King's House  | Kings Road West | Newbury    | RG14 5BY |
      | Queen's House | Kings Road West | Newbury    | RG14 5BY |
      | The Malthouse | Elevator Road   | Manchester | M17 1BR  |
      | Holland House | Bury Street     | London     | EC3A 5AW |
    When user asks for the address with an invalid ID
    Then user receives status code of 404

  Scenario: POST an address
    Given the collection of addresses
      | building      | street          | town       | postcode |
      | King's House  | Kings Road West | Newbury    | RG14 5BY |
      | The Malthouse | Elevator Road   | Manchester | M17 1BR  |
    When user posts an address
      | building      | street      | town   | postcode |
      | Holland House | Bury Street | London | EC3A 5AW |
    Then user receives status code of 201
    When user asks for the address with the latest ID
    Then user receives status code of 200
    And address returned
      | building      | street      | town   | postcode |
      | Holland House | Bury Street | London | EC3A 5AW |

  Scenario: PUT an address
    Given the collection of addresses
      | building      | street          | town       | postcode |
      | The Malthouse | Elevator Road   | Manchester | M17 1BR  |
      | Holland House | Bury Street     | London     | EC3A 5AW |
      | King's House  | Kings Road West | Newbury    | RG14 5BY |
    When user updates the address with the latest ID
      | building      | street          | town    | postcode |
      | Queen's House | Kings Road West | Newbury | RG14 5BY |
    Then user receives status code of 200
    When user asks for all addresses
    Then user receives status code of 200
    And collection of addresses returned
      | building      | street          | town       | postcode |
      | The Malthouse | Elevator Road   | Manchester | M17 1BR  |
      | Holland House | Bury Street     | London     | EC3A 5AW |
      | Queen's House | Kings Road West | Newbury    | RG14 5BY |

  Scenario: PUT an address that does not exist
    Given the collection of addresses
      | building      | street          | town       | postcode |
      | King's House  | Kings Road West | Newbury    | RG14 5BY |
      | The Malthouse | Elevator Road   | Manchester | M17 1BR  |
      | Holland House | Bury Street     | London     | EC3A 5AW |
    When user updates the address with an invalid ID
      | building      | street          | town    | postcode |
      | Queen's House | Kings Road West | Newbury | RG14 5BY |
    Then user receives status code of 404

  Scenario: DELETE an address
    Given the collection of addresses
      | building      | street          | town       | postcode |
      | King's House  | Kings Road West | Newbury    | RG14 5BY |
      | The Malthouse | Elevator Road   | Manchester | M17 1BR  |
      | Holland House | Bury Street     | London     | EC3A 5AW |
    When user deletes the address with the latest ID
    Then user receives status code of 204
    When user asks for all addresses
    Then user receives status code of 200
    And collection of addresses returned
      | building      | street          | town       | postcode |
      | King's House  | Kings Road West | Newbury    | RG14 5BY |
      | The Malthouse | Elevator Road   | Manchester | M17 1BR  |

  Scenario: DELETE an address that does not exist
    Given the collection of addresses
      | building      | street          | town       | postcode |
      | King's House  | Kings Road West | Newbury    | RG14 5BY |
      | The Malthouse | Elevator Road   | Manchester | M17 1BR  |
      | Holland House | Bury Street     | London     | EC3A 5AW |
    When user deletes the address with an invalid ID
    Then user receives status code of 404
