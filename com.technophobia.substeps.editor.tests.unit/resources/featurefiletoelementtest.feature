Feature: Test Feature

  Background: Some background
  	Given a background step
  	 When the background
  	 Then a background result
  	 
  Scenario: 1st scenario
    Given a 1st scenario
     When a 1st scenario
     Then a 1st scenario
     
  Scenario Outline: 1st scenario outline
  	Given a 1st scenario outline
     When a 1st scenario outline
     Then a 1st scenario outline
     
  Examples:
  	|key 1  |key 2  |key 3  |
  	|cell 11|cell 12|cell 13|
  	|cell 21|cell 22|cell 23|
  	|cell 31|cell 32|cell 33|