# Auth Analyzer - Professional Authorization Testing Tool

[![Java](https://img.shields.io/badge/Java-1.8+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-red.svg)](https://maven.apache.org/)
[![Burp Suite](https://img.shields.io/badge/Burp%20Suite-Professional%20%7C%20Community-blue.svg)](https://portswigger.net/burp)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**中文版本**: [README.md](README.md)

## Overview

**Auth Analyzer** is a professional Burp Suite extension designed for authorization testing and security analysis. This powerful tool helps identify authorization vulnerabilities by automatically repeating requests with different user sessions and comparing responses to detect potential bypasses.

### Key Features

🔒 **Multi-Session Management** - Test multiple user roles and privilege levels simultaneously
🎯 **Intelligent Parameter Extraction** - Automatically extract tokens, cookies, and parameters from responses
🔧 **Advanced Parameter Replacement** - Support for JSON Path, Form data, and XML parameter manipulation
📊 **Response Analysis Comparison** - Automatically compare original and modified request responses
🚨 **Bypass Detection** - Built-in analysis engine to identify potential authorization bypasses
📤 **Postman Export** - Export requests and responses to Postman Collection v2.1 format
🎛️ **Flexible Filtering System** - Multiple filters to precisely control testing scope
💾 **Configuration Persistence** - Auto-save and load session configurations

## Table of Contents

- [Installation](#installation)
- [Quick Start](#quick-start)
- [Core Features](#core-features)
- [Advanced Features](#advanced-features)
- [Technical Details](#technical-details)
- [Project Structure](#project-structure)
- [Usage Examples](#usage-examples)
- [Contributing](#contributing)
- [Changelog](#changelog)
- [FAQ](#faq)
- [License](#license)
- [Disclaimer](#disclaimer)

## Installation

### Prerequisites

- **Java**: 1.8 or higher
- **Burp Suite**: Professional or Community edition
- **Maven**: 3.6+ (required for compilation only)

### Build from Source

1. **Clone the repository**
   ```bash
   git clone https://github.com/GitHubNull/my_auth_analyzer.git
   cd my_auth_analyzer
   ```

2. **Build the project**
   ```bash
   mvn clean package
   ```

3. **Install in Burp Suite**
   - Launch Burp Suite
   - Navigate to `Extender` → `Extensions`
   - Click `Add` → `Extension`
   - Select `target/myAuthAnalyzer-2.0.0-jar-with-dependencies.jar`

## Quick Start

### Basic Workflow

1. **Create Sessions**
   - Create separate sessions for each user role you want to test
   - Configure session headers and parameter replacement rules

2. **Configure Parameters**
   - Set up parameters to extract and replace
   - Choose extraction methods (auto/static/prompt input)

3. **Set Filters**
   - Configure request filtering rules
   - Define testing scope

4. **Start Testing**
   - Launch Auth Analyzer
   - Browse the application with a privileged user
   - Monitor test results in real-time

## Core Features

### 🔍 Parameter Extraction System

#### Automatic Extraction
- Extract session cookies from `Set-Cookie` headers
- Extract CSRF tokens from HTML input fields
- Extract dynamic parameters from JSON responses

#### From-To String Extraction
- Generic extraction based on start and end strings
- Support for extracting values from JavaScript variables
- Flexible positioning rule configuration

#### Other Extraction Methods
- Static value predefined
- User interactive input prompts

### 🛠️ Advanced Parameter Replacement

#### JSON Parameter Replacement
Support for standard JSON Path syntax with precise JSON manipulation capabilities:

```json
// Example JSON Path expressions
$.user.name              // Get username
$.store.book[0].title    // Get first book title
$..price                 // Recursive search for all price fields
$.items[*].id            // Get all item IDs
```

**Features**:
- Nested object handling
- Array element operations
- Conditional filtering support
- Complete parameter removal functionality

#### Form Parameter Replacement
Support for two mainstream form formats:

- **URL-Encoded Forms**: `application/x-www-form-urlencoded` format
- **Multipart Forms**: `multipart/form-data` format (including file uploads)

#### XML Parameter Replacement
- XPath expression support
- XML document structure manipulation

### 📈 Bypass Detection Mechanism

Automated response analysis system:

- **SAME**: Response body and status code are identical
- **SIMILAR**: Same status code, response body length differs by ±5%
- **DIFFERENT**: All other cases
- **BYPASS**: Authorization bypass detected

### 🎛️ Intelligent Filtering System

Multiple filters to precisely control testing scope:

- **Scope Filter** (In Scope)
- **Proxy Traffic Filter** (Only Proxy Traffic)
- **File Type Filter** (Exclude Filetypes)
- **HTTP Method Filter** (Exclude HTTP Methods)
- **Status Code Filter** (Exclude Status Codes)
- **Path Filter** (Exclude Paths)
- **Query Parameter Filter** (Exclude Queries/Params)

### 📤 Postman Export Feature

#### Export Capabilities
- **Postman Collection v2.1 Format**: Full compliance with Postman specification
- **Session Organization**: Automatically group requests by session name
- **Metadata Preservation**: Include session info, bypass status, and response codes
- **Flexible Export Options**: Choice to include original requests

#### Export Format Example
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

## Advanced Features

### JSON Path Parameter Replacement Examples

**Scenario**: Replace user ID in nested JSON

```
Configuration:
- JSON Path: $.user.id
- Replace Value: 12345
- Remove: No
```

**Scenario**: Remove authentication token

```
Configuration:
- JSON Path: $.auth.token
- Replace Value: (empty)
- Remove: Yes
```

### Multi-Role Simultaneous Testing

Create multiple sessions to test different privilege levels:

- **Admin Session**: Full privilege access
- **User Session**: Limited privilege access
- **Anonymous Session**: No privilege access

### CORS Configuration Testing

1. Add `Origin` header in session configuration
2. Select `Test CORS` option
3. Auth Analyzer automatically changes HTTP method to OPTIONS
4. Analyze CORS response headers

### CSRF Protection Testing

1. Configure parameter removal functionality
2. Remove CSRF token parameter
3. Observe if requests are properly rejected

## Technical Details

### Dependencies

- **Burp Extender API (2.3)**: Burp Suite extension API
- **Gson (2.10.1)**: JSON serialization/deserialization
- **JSON Path (2.9.0)**: JSON path query and manipulation
- **JSoup (1.15.4)**: HTML parsing and processing
- **FastJSON (2.0.32)**: Additional JSON processing support
- **Apache Tika (2.7.0)**: Content type detection
- **Commons Codec (1.17.1)**: Encoding operations

### Build Configuration

- **Java Version**: 1.8 (source and target)
- **Packaging**: JAR with dependencies
- **Encoding**: UTF-8
- **Build Command**: `mvn package`

### Performance Features

- **Asynchronous Processing**: Multi-threaded concurrent request handling
- **Memory Optimization**: Efficient data structure design
- **Response Caching**: Smart caching mechanism to reduce duplicate computations

## Project Structure

```
src/
├── burp/
│   └── BurpExtender.java                    # Extension entry point
└── com/protect7/authanalyzer/
    ├── entities/                            # Core data entities
    │   ├── Session.java                     # Session entity
    │   ├── Token.java                       # Token entity
    │   ├── JsonParameterReplace.java        # JSON parameter replacement
    │   ├── FormParameterReplace.java        # Form parameter replacement
    │   └── XmlParameterReplace.java         # XML parameter replacement
    ├── gui/                                 # Graphical user interface
    │   ├── main/                            # Main UI components
    │   ├── dialog/                          # Dialog windows
    │   └── entity/                          # UI entity components
    ├── controller/                          # Business logic controllers
    │   ├── HttpListener.java                # HTTP listener
    │   ├── RequestController.java           # Request controller
    │   └── ContextMenuController.java       # Context menu controller
    ├── filter/                              # Request filters
    │   ├── MethodFilter.java                # HTTP method filtering
    │   ├── StatusCodeFilter.java            # Status code filtering
    │   └── PathFilter.java                  # Path filtering
    └── util/                                # Utility classes
        ├── RequestModifHelper.java          # Request modification helper
        ├── DataExporter.java                # Data exporter
        ├── PostmanCollectionBuilder.java    # Postman collection builder
        └── ExtractionHelper.java            # Extraction helper
```

## Usage Examples

### Auto Extract Session Cookie
Define username and password as `static value`. Session cookie name must be defined as `auto extract`. Verify that you start navigating through the application with no session cookie set. Login to the web app. Auth Analyzer will repeat the login request with the static parameters and automatically gets the session by the `Set-Cookie` header.

### Session Header and CSRF Token Parameter
Define a Cookie header and a CSRF token (with `auto value extract`). The CSRF token value will be extracted if it is present in an `HTML Input Tag`, a `Set-Cookie Header` or a `JSON Response` of the given session.

### Auto Extract from JavaScript Variable
Use the generic extraction method called `From To String`. With this extraction method we can extract any value from a response if it is located between a unique starting and ending string.

## Contributing

We welcome community contributions! Please follow these steps:

### Development Setup

1. Fork the project to your GitHub account
2. Clone your fork
3. Create a feature branch: `git checkout -b feature/amazing-feature`
4. Commit your changes: `git commit -m 'Add amazing feature'`
5. Push the branch: `git push origin feature/amazing-feature`
6. Create a Pull Request

### Code Standards

- Follow Java coding conventions
- Add appropriate comments and documentation
- Ensure all tests pass
- Maintain clean code structure

### Issue Reporting

When reporting issues via GitHub Issues, please include:

- Detailed error description
- Steps to reproduce
- Environment information (Java version, Burp Suite version, etc.)
- Relevant logs or screenshots

## Changelog

### v2.0.0
- ✨ Added Postman Collection v2.1 export functionality
- 🔧 Optimized JSON Path parameter replacement engine
- 🎛️ Enhanced filtering system
- 🐛 Fixed parameter extraction edge cases
- 📚 Improved documentation and examples

### v1.1.14
- 🐛 Fixed session configuration saving issues
- 🔧 Optimized response analysis algorithm
- 📤 Improved export functionality
- 🎨 UI interface optimization

## FAQ

### Q: How to extract values from JavaScript variables?
A: Use "From To String" extraction method by setting start and end strings to locate variable values.

### Q: What JSON Path syntax is supported?
A: Standard JSON Path syntax is supported, including nested objects, array indices, recursive search, etc.

### Q: How to test CSRF protection mechanisms?
A: Configure parameter removal functionality to remove CSRF token parameters and observe if requests are properly rejected.

### Q: What export formats are supported for Postman?
A: Currently supports Postman Collection v2.1 format, fully compatible with Postman import.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Disclaimer

This tool is intended for legitimate security research and authorized testing only. Users should:

- Only use on systems where they have explicit permission
- Comply with relevant laws and regulations
- Accept responsibility for any consequences arising from tool usage

For more details, please refer to [DISCLAIMER.md](DISCLAIMER.md).

## Contact

- **Project Homepage**: https://github.com/GitHubNull/my_auth_analyzer
- **Issue Tracker**: https://github.com/GitHubNull/my_auth_analyzer/issues
- **Author**: org 0xff (Enhancement features developer)

---

**Disclaimer**: This tool is intended for authorized security testing and research purposes only. Users must ensure they operate within legal and ethical boundaries.