# Smart Student Hub

A comprehensive Android application designed to enhance the student experience by providing a centralized platform for managing courses, announcements, documents, and user profiles with Google OAuth authentication.

## 🚀 Features

### 🔐 Authentication
- **Google OAuth Integration**: Secure login using Google Sign-In
- **Automatic Session Management**: Remembers user login state
- **Profile Management**: User profile with Google account integration

### 📚 Core Functionality
- **Dashboard**: Overview of recent activities and quick access to features
- **Course Management**: View and manage enrolled courses
- **Announcements**: Stay updated with important notifications categorized by type (URGENT, EVENT, MISC)
- **Document Sharing**: Upload, view, and share documents with other users
- **User Profile**: Manage personal information and preferences
- **Settings**: App configuration, theme selection, and account management

### 🎨 User Interface
- **Material Design 3**: Modern and intuitive user interface
- **Navigation Drawer**: Easy navigation between different sections
- **TabLayout**: Organized content presentation
- **Dark/Light Theme**: Automatic theme switching based on system preferences
- **Responsive Design**: Optimized for different screen sizes

## 🏗️ Architecture

### Design Pattern
- **MVVM (Model-View-ViewModel)**: Clean separation of concerns
- **Repository Pattern**: Centralized data management
- **LiveData & ViewBinding**: Reactive UI updates

### Technology Stack
- **Language**: Kotlin
- **UI Framework**: Android Views with Material Design Components
- **Database**: Room (SQLite) with KSP annotation processing
- **Networking**: Retrofit2 + OkHttp3 for REST API calls
- **Authentication**: Google Play Services Auth
- **Image Loading**: Glide
- **Asynchronous Programming**: Kotlin Coroutines
- **Dependency Injection**: Manual DI with Repository pattern

## 📱 App Structure

### Main Components

#### Activities
- **AuthActivity**: Handles Google OAuth login flow
- **MainActivity**: Main application container with navigation drawer

#### Fragments
- **DashboardFragment**: Home screen with overview
- **CoursesFragment**: Course management interface
- **AnnouncementsFragment**: Announcements display with filtering
- **DocumentsFragment**: Document upload and sharing
- **ProfileFragment**: User profile management
- **SettingsFragment**: App settings and preferences

#### Data Layer
- **Room Database**: Local data persistence
  - User, Course, Announcement, Document entities
  - DAO interfaces for database operations
  - Type converters for complex data types
- **Repositories**: Data access abstraction layer
- **ContentProvider**: Document sharing with other apps

### Key Features Implementation

#### 🔒 Security
- **ProGuard**: Code obfuscation and optimization for release builds
- **Content Provider**: Secure document sharing with URI permissions
- **OAuth 2.0**: Industry-standard authentication

#### 📄 Document Management
- **File Upload**: Support for various document types
- **Content Provider**: Share documents with other applications
- **Metadata Storage**: Document information stored in Room database
- **File Access Control**: Secure file access through ContentProvider

#### 🎨 UI/UX
- **ViewBinding**: Type-safe view references
- **RecyclerView**: Efficient list displays with custom adapters
- **Material Components**: Consistent design language
- **Navigation Component**: Structured navigation flow

## 🛠️ Setup and Installation

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK API 24+ (Android 7.0)
- Google Services configuration
- JDK 11 or later

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone [repository-url]
   cd miniProjet
   ```

2. **Configure Google Services**
   - Add your `google-services.json` file to the `app/` directory
   - Configure OAuth 2.0 credentials in Google Cloud Console

3. **Build the project**
   ```bash
   ./gradlew clean assembleDebug
   ```

4. **Run the application**
   - Connect an Android device or start an emulator
   - Run the app from Android Studio or use:
   ```bash
   ./gradlew installDebug
   ```

## 📋 Configuration

### Build Configuration
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 33 (Android 13)
- **Compile SDK**: API 33
- **Java Version**: 11
- **Kotlin Version**: 1.9.0

### Dependencies
- **AndroidX Libraries**: Core, AppCompat, Material Design
- **Room Database**: 2.5.2 with KSP annotation processing
- **Retrofit**: 2.9.0 for networking
- **Google Play Services**: Auth 20.6.0
- **Glide**: 4.15.1 for image loading
- **Kotlin Coroutines**: 1.7.1

## 🔧 Development

### Code Style
- **Kotlin Coding Conventions**: Following official Kotlin style guide
- **MVVM Pattern**: Clear separation between UI and business logic
- **Repository Pattern**: Centralized data access

### Testing
- **Unit Tests**: JUnit 4.13.2
- **UI Tests**: Espresso 3.5.1
- **Test Coverage**: Focus on business logic and data layer

### Build Variants
- **Debug**: Development build with debugging enabled
- **Release**: Production build with ProGuard optimization

## 📚 API Integration

### Google Classroom API
- Course information retrieval
- Announcement synchronization
- Student data management

### REST API Support
- Retrofit configuration for external APIs
- JSON serialization with Gson
- HTTP logging for debugging

## 🔐 Security Features

### Data Protection
- **Local Database Encryption**: Room database with secure storage
- **Network Security**: HTTPS-only communication
- **ProGuard**: Code obfuscation for release builds

### Authentication
- **OAuth 2.0**: Secure Google authentication
- **Token Management**: Automatic token refresh
- **Session Persistence**: Secure session storage

## 📖 Usage

### First Launch
1. Open the app
2. Sign in with your Google account
3. Grant necessary permissions
4. Navigate through the app using the drawer menu

### Daily Usage
- **Check Dashboard**: View recent activities and updates
- **Browse Courses**: Access course materials and information
- **Read Announcements**: Stay updated with important notifications
- **Manage Documents**: Upload and share study materials
- **Update Profile**: Keep your information current

## 🤝 Contributing

### Development Workflow
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

### Code Guidelines
- Follow Kotlin coding conventions
- Write meaningful commit messages
- Include tests for new features
- Update documentation as needed

## 📄 License

This project is developed for educational purposes as part of a Kotlin/Android development course.

## 📁 Project Structure

```
app/
├── src/main/
│   ├── java/com/example/miniprojet/
│   │   ├── auth/                    # Authentication management
│   │   │   └── GoogleAuthManager.kt
│   │   ├── data/                    # Data layer
│   │   │   ├── local/              # Local database
│   │   │   │   ├── dao/            # Data Access Objects
│   │   │   │   ├── AppDatabase.kt
│   │   │   │   ├── StudentHubDatabase.kt
│   │   │   │   └── DocumentContentProvider.kt
│   │   │   ├── model/              # Data models
│   │   │   ├── remote/             # Network layer
│   │   │   └── repository/         # Repository pattern
│   │   ├── ui/                     # User Interface
│   │   │   ├── announcements/
│   │   │   ├── auth/
│   │   │   ├── courses/
│   │   │   ├── dashboard/
│   │   │   ├── documents/
│   │   │   ├── profile/
│   │   │   └── settings/
│   │   ├── utils/                  # Utility classes
│   │   ├── MainActivity.kt
│   │   └── SmartStudentHubApp.kt
│   ├── res/                        # Resources
│   │   ├── layout/                 # XML layouts
│   │   ├── values/                 # Strings, colors, themes
│   │   ├── drawable/               # Images and icons
│   │   ├── menu/                   # Menu definitions
│   │   └── xml/                    # XML configurations
│   └── AndroidManifest.xml
├── build.gradle.kts                # Module build configuration
└── proguard-rules.pro             # ProGuard configuration
```

## 🔄 Data Flow

### Authentication Flow
1. **App Launch** → AuthActivity checks for existing session
2. **Login Required** → Google OAuth flow initiated
3. **Success** → User data stored, navigate to MainActivity
4. **Session Management** → Automatic token refresh

### Data Synchronization
1. **Local First** → Data served from Room database
2. **Background Sync** → API calls update local data
3. **Conflict Resolution** → Server data takes precedence
4. **Offline Support** → App functions with cached data

## 🎯 Key Design Decisions

### Why KSP over KAPT?
- **Performance**: 2x faster build times
- **Incremental Processing**: Only processes changed files
- **Future-Proof**: Official Kotlin annotation processing tool
- **Better Error Messages**: More detailed compilation errors

### Why Room Database?
- **Type Safety**: Compile-time SQL verification
- **LiveData Integration**: Reactive UI updates
- **Migration Support**: Database schema evolution
- **Performance**: Optimized SQLite wrapper

### Why MVVM Architecture?
- **Separation of Concerns**: Clear responsibility boundaries
- **Testability**: Easy to unit test business logic
- **Lifecycle Awareness**: Automatic UI updates
- **Maintainability**: Easier to modify and extend

## 🚨 Known Issues & Limitations

### Current Limitations
- **Offline Mode**: Limited functionality without internet
- **File Size**: Document upload size restrictions
- **API Rate Limits**: Google Classroom API quotas
- **Device Storage**: Large documents may impact performance

### Planned Improvements
- **Push Notifications**: Real-time announcement alerts
- **Offline Sync**: Better offline data management
- **File Preview**: In-app document viewing
- **Search Functionality**: Global search across all content

## 📊 Performance Considerations

### Optimization Strategies
- **Lazy Loading**: Content loaded on demand
- **Image Caching**: Glide for efficient image management
- **Database Indexing**: Optimized query performance
- **ProGuard**: Code shrinking and obfuscation

### Memory Management
- **ViewBinding**: Prevents memory leaks
- **Lifecycle Awareness**: Proper resource cleanup
- **Coroutines**: Efficient background processing
- **Pagination**: Large datasets handled efficiently

## 🔍 Troubleshooting

### Common Issues

#### Build Errors
```bash
# Clean and rebuild
./gradlew clean
./gradlew assembleDebug
```

#### Google Sign-In Issues
- Verify `google-services.json` is in `app/` directory
- Check OAuth 2.0 configuration in Google Cloud Console
- Ensure correct package name and SHA-1 fingerprint

#### Database Issues
- Clear app data if schema changes
- Check Room migration scripts
- Verify entity annotations

### Debug Tools
- **Android Studio Debugger**: Step-through debugging
- **Database Inspector**: View Room database content
- **Network Inspector**: Monitor API calls
- **Layout Inspector**: UI debugging

## 📞 Support

For questions or issues, please refer to the course materials or contact the development team.

## 🙏 Acknowledgments

- **Google**: For Android development tools and APIs
- **JetBrains**: For Kotlin programming language
- **Material Design**: For UI/UX guidelines
- **Open Source Community**: For various libraries used

---

**Smart Student Hub** - Enhancing the student experience through technology 🎓
