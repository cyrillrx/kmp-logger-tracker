# Android logger and tracker components
A modular logger component.

# Publish to Bintray

## Logger modules

### Release note
Update the release notes: Logger/release_notes.txt

### Build script
```
./gradlew clean build bintrayUpload -p Logger -PbintrayUser=cyrillrx -PbintrayKey=KEY -PdryRun=false
./gradlew clean build bintrayUpload -p Logger-extension -PbintrayUser=cyrillrx -PbintrayKey=KEY -PdryRun=false

```

## Tracker modules

### Release note
Update the release notes: Tracker/release_notes.txt

### Build script
```
./gradlew clean build bintrayUpload -p Tracker -PbintrayUser=cyrillrx -PbintrayKey=KEY -PdryRun=false
./gradlew clean build bintrayUpload -p Tracker-extension -PbintrayUser=cyrillrx -PbintrayKey=KEY -PdryRun=false

```