Tags: release-1 editor formatting
Feature: feature file content formatting
		feature files in eclipse can be formatted in a consistent style

  Background:
	Given the "Welcome" view is not visible
	Given I am working on the "Java" Perspective
	Given there is a general project named "Test project"
	Given I am working in a new editor named "content-formatter-file.feature" in project "Test project"

  Scenario Outline: Indentation of feature block
	Given the text from file "<InputFile>" exists in the editor
	 When I format the contents of the editor
	 Then the editor contents match file "<OutputFile>"

  Examples:
	|InputFile									|OutputFile|
	|contentformat/input/unindented-feature	|contentformat/output/indented-feature|
	|contentformat/input/indented-feature	|contentformat/output/indented-feature|

  Scenario Outline: Indentation of background block
	Given the text from file "<InputFile>" exists in the editor
	 When I format the contents of the editor
	 Then the editor contents match file "<OutputFile>"

  Examples:
	|InputFile									|OutputFile|
	|contentformat/input/unindented-background	|contentformat/output/indented-background|
	|contentformat/input/indented-background	|contentformat/output/indented-background|

  Scenario Outline: Indentation of scenario block
	Given the text from file "<InputFile>" exists in the editor
	 When I format the contents of the editor
	 Then the editor contents match file "<OutputFile>"

  Examples:
	|InputFile									|OutputFile|
	|contentformat/input/unindented-scenario	|contentformat/output/indented-scenario|
	|contentformat/input/indented-scenario		|contentformat/output/indented-scenario|

  Scenario Outline: Indentation of scenario outline
	Given the text from file "<InputFile>" exists in the editor
	 When I format the contents of the editor
	 Then the editor contents match file "<OutputFile>"

  Examples:
	|InputFile											|OutputFile|
	|contentformat/input/unindented-scenario-outline	|contentformat/output/indented-scenario-outline|
	|contentformat/input/indented-scenario-outline		|contentformat/output/indented-scenario-outline|

  Scenario Outline: Indentation of comments relative to lower block
	Given the text from file "<InputFile>" exists in the editor
	 When I format the contents of the editor
	 Then the editor contents match file "<OutputFile>"

  Examples:
	|InputFile															|OutputFile|
	|contentformat/input/unindented-comment-before-feature				|contentformat/output/indented-comment-before-feature|
	|contentformat/input/indented-comment-before-feature				|contentformat/output/indented-comment-before-feature|
	|contentformat/input/unindented-comment-before-background			|contentformat/output/indented-comment-before-background|
	|contentformat/input/indented-comment-before-background			|contentformat/output/indented-comment-before-background|
	|contentformat/input/unindented-comment-before-scenario			|contentformat/output/indented-comment-before-scenario|
	|contentformat/input/indented-comment-before-scenario				|contentformat/output/indented-comment-before-scenario|
	|contentformat/input/unindented-comment-before-scenario-outline	|contentformat/output/indented-comment-before-scenario-outline|
	|contentformat/input/indented-comment-before-scenario-outline		|contentformat/output/indented-comment-before-scenario-outline|

  Scenario Outline: Indentation of tags relative to lower block
	Given the text from file "<InputFile>" exists in the editor
	 When I format the contents of the editor
	 Then the editor contents match file "<OutputFile>"

  Examples:
	|InputFile														|OutputFile|
	|contentformat/input/unindented-tags-before-feature			|contentformat/output/indented-tags-before-feature|
	|contentformat/input/indented-tags-before-feature				|contentformat/output/indented-tags-before-feature|
	|contentformat/input/unindented-tags-before-scenario			|contentformat/output/indented-tags-before-scenario|
	|contentformat/input/indented-tags-before-scenario				|contentformat/output/indented-tags-before-scenario|
	|contentformat/input/unindented-tags-before-scenario-outline	|contentformat/output/indented-tags-before-scenario-outline|
	|contentformat/input/indented-tags-before-scenario-outline		|contentformat/output/indented-tags-before-scenario-outline|

  Scenario Outline: Adequate newline between feature and background
	Given the text from file "<InputFile>" exists in the editor
	 When I format the contents of the editor
	 Then the editor contents match file "<OutputFile>"

  Examples:
	|InputFile															|OutputFile|
	|contentformat/input/no-newline-between-feature-and-background		|contentformat/output/newline-between-feature-and-background|
	|contentformat/input/newline-between-feature-and-background			|contentformat/output/newline-between-feature-and-background|
	|contentformat/input/2-newlines-between-feature-and-background		|contentformat/output/newline-between-feature-and-background|

  Scenario Outline: Adequate newline between background and scenario
	Given the text from file "<InputFile>" exists in the editor
	 When I format the contents of the editor
	 Then the editor contents match file "<OutputFile>"

  Examples:
	|InputFile															|OutputFile|
	|contentformat/input/no-newline-between-background-and-scenario	|contentformat/output/newline-between-background-and-scenario|
	|contentformat/input/newline-between-background-and-scenario		|contentformat/output/newline-between-background-and-scenario|
	|contentformat/input/2-newlines-between-background-and-scenario	|contentformat/output/newline-between-background-and-scenario|

  Scenario Outline: Adequate newline between background and scenario outline
	Given the text from file "<InputFile>" exists in the editor
	 When I format the contents of the editor
	 Then the editor contents match file "<OutputFile>"

  Examples:
	|InputFile																	|OutputFile|
	|contentformat/input/no-newline-between-background-and-scenario-outline	|contentformat/output/newline-between-background-and-scenario-outline|
	|contentformat/input/newline-between-background-and-scenario-outline		|contentformat/output/newline-between-background-and-scenario-outline|
	|contentformat/input/2-newlines-between-background-and-scenario-outline	|contentformat/output/newline-between-background-and-scenario-outline|

  Scenario Outline: Adequate newline between scenario outline and examples
	Given the text from file "<InputFile>" exists in the editor
	 When I format the contents of the editor
	 Then the editor contents match file "<OutputFile>"

  Examples:
	|InputFile																|OutputFile|
	|contentformat/input/no-newline-between-scenario-outline-and-examples	|contentformat/output/newline-between-scenario-outline-and-examples|
	|contentformat/input/newline-between-scenario-outline-and-examples		|contentformat/output/newline-between-scenario-outline-and-examples|
	|contentformat/input/2-newlines-between-scenario-outline-and-examples	|contentformat/output/newline-between-scenario-outline-and-examples|

  Scenario Outline: Adequate newline between scenario and given
	Given the text from file "<InputFile>" exists in the editor
	 When I format the contents of the editor
	 Then the editor contents match file "<OutputFile>"

  Examples:
	|InputFile													|OutputFile|
	|contentformat/input/no-newline-between-scenario-and-given	|contentformat/output/no-newline-between-scenario-and-given|
	|contentformat/input/newline-between-scenario-and-given	|contentformat/output/no-newline-between-scenario-and-given|

  Scenario Outline: Adequate newline between given, when, then and and
	Given the text from file "<InputFile>" exists in the editor
	 When I format the contents of the editor
	 Then the editor contents match file "<OutputFile>"

  Examples:
	|InputFile													|OutputFile|
	|contentformat/input/no-newline-between-given-and-when	|contentformat/output/no-newline-between-given-and-when|
	|contentformat/input/newline-between-given-and-when	|contentformat/output/no-newline-between-given-and-when|
	|contentformat/input/no-newline-between-given-and-and	|contentformat/output/no-newline-between-given-and-and|
	|contentformat/input/newline-between-given-and-and	|contentformat/output/no-newline-between-given-and-and|
	|contentformat/input/no-newline-between-given-and-then	|contentformat/output/no-newline-between-given-and-then|
	|contentformat/input/newline-between-given-and-then	|contentformat/output/no-newline-between-given-and-then|
	|contentformat/input/no-newline-between-and-and-when	|contentformat/output/no-newline-between-and-and-when|
	|contentformat/input/newline-between-and-and-when	|contentformat/output/no-newline-between-and-and-when|
	|contentformat/input/no-newline-between-and-and-and	|contentformat/output/no-newline-between-and-and-and|
	|contentformat/input/newline-between-and-and-and	|contentformat/output/no-newline-between-and-and-and|
	|contentformat/input/no-newline-between-when-and-and	|contentformat/output/no-newline-between-when-and-and|
	|contentformat/input/newline-between-when-and-and	|contentformat/output/no-newline-between-when-and-and|
	|contentformat/input/no-newline-between-when-and-then	|contentformat/output/no-newline-between-when-and-then|
	|contentformat/input/newline-between-when-and-then	|contentformat/output/no-newline-between-when-and-then|
	|contentformat/input/no-newline-between-and-and-then	|contentformat/output/no-newline-between-and-and-then|
	|contentformat/input/newline-between-and-and-then	|contentformat/output/no-newline-between-and-and-then|
	|contentformat/input/no-newline-between-then-and-and	|contentformat/output/no-newline-between-then-and-and|
	|contentformat/input/newline-between-then-and-and	|contentformat/output/no-newline-between-then-and-and|

  Scenario Outline: Adequate newline between examples and example row
	Given the text from file "<InputFile>" exists in the editor
	 When I format the contents of the editor
	 Then the editor contents match file "<OutputFile>"

  Examples:
	|InputFile															|OutputFile|
	|contentformat/input/no-newline-between-examples-and-example-row	|contentformat/output/no-newline-between-examples-and-example-row|
	|contentformat/input/newline-between-examples-and-example-row		|contentformat/output/no-newline-between-examples-and-example-row|

  Scenario Outline: Adequate newline between scenarios
	Given the text from file "<InputFile>" exists in the editor
	 When I format the contents of the editor
	 Then the editor contents match file "<OutputFile>"

  Examples:
	|InputFile											|OutputFile|
	|contentformat/input/no-newline-between-scenarios	|contentformat/output/newline-between-scenarios|
	|contentformat/input/newline-between-scenarios		|contentformat/output/newline-between-scenarios|
	|contentformat/input/2-lines-between-scenarios		|contentformat/output/newline-between-scenarios|