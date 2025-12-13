# AI Story Generator

## Team Members
Rayan Zafar and Shane Uriarte

## Setup
 - Get OpenAI API key from https://platform.openai.com/api-keys
 - Insert the API key into config.properties (outlined in config.properties.example)
 - Run Main.java in org.example

## How to Run
1. Insert OpenAI API key into config.properties under resources
2. Run Main.java in org.example

## Tech Stack
- Java
- Java Swing
- JUnit 5
- OpenAI REST API

## Features
 - Different Story Creation Modes - Genre-based, Character-driven, Setting-based, Choose-your-own-adventure
 - Save/Load multiple stories within Library
 - Tag/categorize stories
   
## Design Patterns
 - Strategy: Allows for different story creation modes | Associated with different customization features and nuanced api calls
 - Factory: Input validation
 - Observer: Story listener within controller
 - Singleton: OpenAIService

## Demo
https://youtu.be/_ZNmfSvRMgk?si=tNc198bfsSgSP6r7



