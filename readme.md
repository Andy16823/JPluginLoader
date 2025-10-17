# JPluginLoader

A lightweight Java Plugin Loader for dynamically loading JAR-based plugins at runtime.

## 📋 Overview

JPluginLoader is a simple and efficient Java library that enables applications to dynamically load plugin JAR files at runtime. The library automatically searches for classes that implement a specific interface and instantiates them as plugins.

## ✨ Features

- **Dynamic Plugin Loading**: Load JAR files at runtime without application restart
- **Interface-based**: Automatically searches for classes implementing a specific interface
- **Type Safety**: Generic implementation for type-safe plugin handling
- **Error Handling**: Robust error handling during plugin loading
- **Simple API**: Intuitive and easy-to-use API
- **Maven Integration**: Fully integrated with Maven

## 🚀 Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Installation

#### Option 1: Via JitPack (Recommended)

Add the JitPack repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Then add the dependency:

```xml
<dependency>
    <groupId>com.github.Andy16823</groupId>
    <artifactId>JPluginLoader</artifactId>
    <version>Tag</version>
</dependency>
```

### Basic Usage

1. **Define Plugin Interface:**

```java
public interface MyPlugin {
    void execute();
    String getName();
}
```

2. **Implement Plugin (in separate JAR):**

```java
public class MyPluginImpl implements MyPlugin {
    @Override
    public void execute() {
        System.out.println("Plugin is executing!");
    }
    
    @Override
    public String getName() {
        return "My First Plugin";
    }
}
```

3. **Load Plugins:**

```java
import com.ad.jpluginloader.JPluginLoader;
import java.util.Vector;

public class Application {
    public static void main(String[] args) {
        try {
            // Create plugin loader
            JPluginLoader loader = new JPluginLoader("./plugins");
            
            // Load plugins
            Vector<MyPlugin> plugins = loader.loadPlugins(MyPlugin.class);
            
            // Execute plugins
            for (MyPlugin plugin : plugins) {
                System.out.println("Found: " + plugin.getName());
                plugin.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## 📁 Project Structure

```
JPluginLoader/
├── pluginloader/
│   ├── src/
│   │   ├── main/java/com/ad/jpluginloader/
│   │   │   └── JPluginLoader.java
│   │   └── test/java/com/ad/pluginloader/
│   │       └── AppTest.java
│   ├── pom.xml
│   └── target/
├── LICENSE
└── readme.md
```

## 🔧 API Reference

### JPluginLoader

#### Constructor
```java
public JPluginLoader(String pluginDir)
```
- `pluginDir`: Path to the directory containing plugin JAR files

#### Methods
```java
public <T> Vector<T> loadPlugins(Class<T> refClass) throws IOException
```
- `refClass`: The interface or base class that plugins must implement
- **Returns**: Vector containing instantiated plugin objects
- **Throws**: `IOException` for file access problems, `IllegalArgumentException` for invalid plugin directory

## 💡 Examples

### Example 1: Simple Plugin System

```java
// Plugin Interface
public interface Calculator {
    double calculate(double a, double b);
    String getOperationName();
}

// Use plugin loader
JPluginLoader loader = new JPluginLoader("./math-plugins");
Vector<Calculator> calculators = loader.loadPlugins(Calculator.class);

for (Calculator calc : calculators) {
    System.out.println(calc.getOperationName() + ": " + calc.calculate(10, 5));
}
```

### Example 2: Plugin with Configuration

```java
public interface ConfigurablePlugin {
    void configure(Properties config);
    void execute();
}

// Load and configure plugins
Vector<ConfigurablePlugin> plugins = loader.loadPlugins(ConfigurablePlugin.class);
Properties config = new Properties();
config.setProperty("setting1", "value1");

for (ConfigurablePlugin plugin : plugins) {
    plugin.configure(config);
    plugin.execute();
}
```

## 🏗️ Build and Test

### Build Project
```bash
cd pluginloader
mvn clean compile
```

### Run Tests
```bash
mvn test
```

### Create JAR
```bash
mvn package
```

## 📋 Plugin Requirements

Plugin JAR files must meet the following criteria:

1. **Interface Implementation**: At least one class must implement the specified interface
2. **Public Constructor**: Plugin classes must have a parameterless public constructor
3. **No Abstract Classes**: Only concrete classes will be instantiated
4. **JAR Format**: Plugins must be provided as JAR files

## ⚠️ Important Notes

- Plugin directory must exist and be readable
- JAR files are loaded with an isolated ClassLoader
- Memory leaks may occur if plugin ClassLoaders are not properly released
- Plugin dependencies must be included in the plugin JAR or available in the main classpath

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the [License](LICENSE).

## 👤 Author

**Andy Designs**
- Website: [andy-designs.de](http://www.andy-designs.de)

## 🔗 Links

- [Java Documentation](https://docs.oracle.com/en/java/)

---

*Made with ❤️ for the Java Community*
