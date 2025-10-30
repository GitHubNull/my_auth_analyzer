# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is **Auth Analyzer**, a Burp Suite extension designed for authorization testing and security analysis. The extension helps identify authorization vulnerabilities by automatically repeating requests with different user sessions and comparing responses to detect potential bypasses.

### Key Features
- **Session Management**: Create multiple user sessions for testing different privilege levels
- **Parameter Extraction**: Auto-extract tokens, cookies, and parameters from responses
- **Advanced Parameter Replacement**: Support for JSON Path, Form data, and XML parameter manipulation
- **Response Analysis**: Automatic comparison of original and modified requests to identify bypasses
- **Bypass Detection**: Built-in analysis to flag potential authorization bypasses
- **Postman Export**: Export requests and responses to Postman Collection v2.1 format for API testing

## Architecture

### Core Components

- **BurpExtender** (`src/burp/BurpExtender.java`): Main extension entry point implementing IBurpExtender
- **MainPanel** (`src/com/protect7/authanalyzer/gui/main/MainPanel.java`): Primary UI container with split-pane layout
- **Session** (`src/com/protect7/authanalyzer/entities/Session.java`): Core entity representing a test session with headers, tokens, and parameters
- **HttpListener** (`src/com/protect7/authanalyzer/controller/HttpListener.java`): Handles HTTP traffic interception and processing
- **RequestController** (`src/com/protect7/authanalyzer/controller/RequestController.java`): Manages request modification and repetition logic

### Key Packages

- `entities/`: Core data models (Session, Token, AnalyzerRequestResponse, various parameter replacement entities)
- `gui/main/`: Main UI components (MainPanel, ConfigurationPanel, CenterPanel)
- `gui/entity/`: UI-specific entity representations (SessionPanel, StatusPanel, TokenPanel)
- `gui/dialog/`: Dialog windows for configuration and detailed operations
- `controller/`: Business logic controllers (HttpListener, RequestController, ContextMenuController)
- `filter/`: Request filtering implementations (MethodFilter, StatusCodeFilter, PathFilter, etc.)
- `util/`: Utility classes for data processing, export, and helper functions

### Advanced Parameter System

The extension supports sophisticated parameter manipulation:

1. **JSON Parameter Replacement**: Uses JSON Path expressions for precise JSON manipulation
2. **Form Parameter Replacement**: Handles both URL-encoded and multipart form data
3. **XML Parameter Replacement**: XML-specific parameter manipulation
4. **Token System**: Flexible token extraction and replacement with multiple extraction methods

## Build and Development

### Maven Build Commands

```bash
# Compile the project
mvn compile

# Run tests
mvn test

# Package into JAR with dependencies
mvn package

# Clean build artifacts
mvn clean

# Install to local repository
mvn install
```

### Key Build Configuration

- **Java Version**: 1.8 (source and target)
- **Packaging**: JAR with dependencies using maven-assembly-plugin
- **Main Dependencies**:
  - Burp Extender API (2.3)
  - Gson (2.10.1) for JSON processing
  - JSON Path (2.9.0) for advanced JSON manipulation
  - JSoup (1.15.4) for HTML parsing
  - FastJSON (2.0.32) for additional JSON support
  - Apache Tika (2.7.0) for content type detection
  - Commons Codec (1.17.1) for encoding operations

### Testing

- **Test Framework**: JUnit 4.13.2
- Test files should be placed in `src/test/java/`
- Run individual tests: `mvn test -Dtest=ClassName`

## Development Guidelines

### Adding New Parameter Types

1. Create new entity class in `entities/` package (following existing patterns like `JsonParameterReplace`)
2. Create corresponding dialog in `gui/dialog/` package
3. Add UI components to `SessionPanel` or relevant UI component
4. Update `RequestModifHelper` to handle the new parameter type
5. Ensure proper serialization/deserialization in `Session` entity

### Adding New Filters

1. Implement the filter interface in `filter/` package
2. Add filter configuration UI to appropriate dialog
3. Update filter processing logic in request handling pipeline
4. Test filter with various request types

### GUI Development

- Use Swing components consistent with existing UI
- Follow the split-pane layout pattern established in `MainPanel`
- Ensure proper integration with `ConfigurationPanel` and `CenterPanel`
- Add new menu items to `AuthAnalyzerMenu` if needed

### Request Processing Pipeline

1. **Interception**: `HttpListener` captures HTTP traffic
2. **Filtering**: Applied through filter chain based on configuration
3. **Parameter Extraction**: Token and parameter extraction from responses
4. **Request Modification**: Headers, parameters, and body modifications applied
5. **Request Execution**: Modified requests sent through Burp's HTTP client
6. **Response Analysis**: Comparison and bypass detection logic
7. **UI Updates**: Results displayed in center panel with status indicators

## Configuration and Data Storage

- Session configurations auto-save to persistent storage
- Export functionality supports XML, HTML, and Postman Collection formats
- Configuration persistence handled through `DataStorageProvider`
- Session data includes request/response history for analysis

## Postman Collection Export

### Overview
The Auth Analyzer now supports exporting requests and responses to Postman Collection v2.1 format, enabling easy integration with Postman for API testing and collaboration.

### Features
- **Session Organization**: Requests are organized in folders by session name
- **Request Naming**: Includes method, path, and bypass status (e.g., "GET /api/users - BYPASS")
- **Metadata Preservation**: Session info, bypass status, and response codes included in descriptions
- **Flexible Export Options**: User choice to include original requests alongside modified test requests
- **Schema Compliance**: Full compliance with Postman Collection v2.1 specification

### Export Process
1. Click "Export Table Data" in the Auth Analyzer interface
2. Select "Postman Collection v2.1" format
3. Choose whether to include original requests (checkbox option)
4. Save as `.json` file
5. Import the collection into Postman

### Collection Structure
```
Auth Analyzer Export/
├── Session: Admin/
│   ├── GET /api/users - BYPASS
│   ├── POST /api/users - SAME
│   └── ...
├── Session: User/
│   ├── GET /api/profile - DIFFERENT
│   └── ...
└── Session: Anonymous/
    ├── GET /api/public - SAME
    └── ...
```

### Technical Implementation
- **PostmanCollectionBuilder**: Main builder class creating collection structure
- **PostmanItemConverter**: Converts HTTP data to Postman request/response format
- **PostmanConstants**: Constants for schema URLs, content types, and naming patterns
- **Integration**: Seamlessly integrated into existing DataExporter and DataExportDialog

### Key Technical Details
- **Dependencies**: Uses existing Gson library (no new Maven dependencies needed)
- **Data Flow**: Leverages existing ExportAuthAnalyzerDataItem structures
- **Format Compliance**: Strict adherence to Postman Collection v2.1 schema
- **User Experience**: Seamless integration with existing export dialog

## Security Considerations

This is a legitimate security testing tool designed for:
- Authorized penetration testing
- Security research and education
- Application security assessment
- Defense and vulnerability identification

The tool should only be used on systems you have explicit permission to test.