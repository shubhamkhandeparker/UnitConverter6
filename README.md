# 🔄 Unit Converter App

A beautiful, modern Android unit converter app built with **Kotlin** and **Jetpack Compose**. Convert between different units across 8 categories with smooth animations and a premium UI design.

![App Demo](https://img.shields.io/badge/Platform-Android-green.svg)
![Language](https://img.shields.io/badge/Language-Kotlin-blue.svg)
![UI](https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg)
![API](https://img.shields.io/badge/Min%20SDK-24-yellow.svg)

## ✨ Features

### 🎯 **8 Conversion Categories**
- 📏 **Length** - Meters, centimeters, inches, feet, yards, miles, kilometers
- ⚖️ **Weight** - Grams, kilograms, pounds, ounces, tons
- 🌡️ **Temperature** - Celsius, Fahrenheit, Kelvin
- 🧪 **Volume** - Liters, milliliters, gallons, quarts, pints, cups, fluid ounces
- 🏃 **Speed** - m/s, km/h, mph, ft/s, knots
- ⏰ **Time** - Seconds, minutes, hours, days, weeks, months, years
- 📐 **Area** - Square meters, square feet, acres, hectares, square inches
- ⚡ **Energy** - Joules, kilojoules, calories, kilocalories, watt hours, kWh, BTU

### 🎨 **Beautiful UI Design**
- **Modern Material 3 Design** with custom pink-purple gradient theme
- **Smooth Animations** - Screen transitions, floating background elements, particle effects
- **Interactive Elements** - Animated swap button with particle burst
- **Responsive Layout** - Works on all Android screen sizes
- **Glassmorphism Effects** - Semi-transparent cards with blur effects

### 🚀 **Performance Features**
- **Real-time Conversion** - Instant results as you type
- **Clean Architecture** - Modular, reusable components
- **Memory Efficient** - Optimized animations and state management
- **Smooth 60fps** - Butter-smooth user experience

## 📱 Screenshots

| Intro Screen | Category Selection | Length Converter | Temperature Converter |
|:---:|:---:|:---:|:---:|
| ![Intro](![image](https://github.com/user-attachments/assets/dec0f147-6734-4b12-b56b-73c3d5b0c463)) 
| ![Categories](![image](https://github.com/user-attachments/assets/8d91b237-52c9-43c7-8d79-2592656950af)) 
| ![Length](![image](https://github.com/user-attachments/assets/ef68db06-34cc-44f7-b5ce-d8e8c8f44225)) 
| ![Temperature](![image](https://github.com/user-attachments/assets/92713889-2e1a-49f1-90cc-9efd2396041c)) |

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM pattern with Compose state management
- **Animation**: Compose Animation API
- **Material Design**: Material 3 components
- **Minimum SDK**: Android 7.0 (API level 24)
- **Target SDK**: Android 14 (API level 34)

## 🏗️ Project Structure

```
app/
├── src/main/java/com/example/unitconverter/
│   ├── MainActivity.kt              # Main activity and UI screens
│   ├── ConversionCategory.kt        # Data class for categories
│   ├── ConversionData.kt           # List of all conversion categories
│   ├── LengthUnits.kt              # Length conversion logic
│   ├── WeightUnits.kt              # Weight conversion logic
│   ├── TemperatureUnits.kt         # Temperature conversion logic
│   ├── VolumeUnits.kt              # Volume conversion logic
│   ├── SpeedUnits.kt               # Speed conversion logic
│   ├── TimeUnits.kt                # Time conversion logic
│   ├── AreaUnits.kt                # Area conversion logic
│   ├── EnergyUnits.kt              # Energy conversion logic
│   └── ui/theme/                   # App theming
```

## 🔧 Installation

### Prerequisites
- Android Studio Arctic Fox or later
- Kotlin 1.8.0 or later
- Android SDK 24 or later

### Setup
1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/unit-converter-app.git
   cd unit-converter-app
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Build and Run**
   - Sync project with Gradle files
   - Connect Android device or start emulator
   - Click Run button or press Ctrl+R

## 🎮 How to Use

1. **Launch the app** and tap "Get Started" on the intro screen
2. **Select a conversion category** from the 8 available options
3. **Enter a value** in the input field
4. **Choose units** from the dropdown menus
5. **View instant results** as you type
6. **Tap the swap button** to exchange from/to units with cool particle effects
7. **Navigate back** using the back button to select different categories

## 🧮 Conversion Logic

Each conversion category uses a **base unit approach** for accurate calculations:

- **Length**: Meter as base unit
- **Weight**: Gram as base unit  
- **Temperature**: Special formulas (C ↔ F ↔ K)
- **Volume**: Liter as base unit
- **Speed**: Meters per second as base unit
- **Time**: Second as base unit
- **Area**: Square meter as base unit
- **Energy**: Joule as base unit

### Example Conversion Flow
```kotlin
// Convert 5 feet to meters
val valueInMeters = 5 * 0.3048  // Convert to base unit (meters)
val result = valueInMeters / 1.0 // Convert to target unit
// Result: 1.524 meters
```

## 🎨 UI Components

### Custom Components
- **FloatingBackgroundElements** - Animated floating bubbles
- **GlowingAppIcon** - Animated app icon with glow effects
- **EnhancedButton** - Gradient button with press animations
- **GenericUnitDropdown** - Reusable dropdown for all unit types
- **ParticleEffect** - Particle burst animation for swap button

### Animations
- **Screen Transitions** - Slide and fade animations between screens
- **Staggered Entrance** - Category cards animate in sequence
- **Floating Elements** - Background bubbles with continuous motion
- **Interactive Feedback** - Button press and particle effects

## 🔄 Adding New Converters

To add a new conversion category:

1. **Create Unit Data Class**
   ```kotlin
   data class YourUnit(
       val name: String,
       val symbol: String,
       val baseRatio: Double
   )
   ```

2. **Define Units List**
   ```kotlin
   val yourUnits = listOf(
       YourUnit("Base Unit", "base", 1.0),
       YourUnit("Other Unit", "other", conversionRatio)
   )
   ```

3. **Add Conversion Function**
   ```kotlin
   fun convertYourUnit(value: Double, fromUnit: YourUnit, toUnit: YourUnit): Double {
       val valueInBase = value * fromUnit.baseRatio
       return valueInBase / toUnit.baseRatio
   }
   ```

4. **Update MainActivity**
   - Add case to `ConverterScreen`
   - Create `YourConverterScreen` function

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Developer

Built with ❤️ by [Your Name]

- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [Your Profile](https://linkedin.com/in/yourprofile)

## 🙏 Acknowledgments

- **Material Design** for the design system
- **Jetpack Compose** team for the amazing UI toolkit
- **Android Developers** community for inspiration and resources

---

⭐ **Star this repo if you found it helpful!** ⭐
