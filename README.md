# Android logger and tracker components
A modular logger component.

# Publish to Bintray

## Logger modules

### Release note
Update the release notes: Logger/release_notes.txt

### Build scripts
```./gradlew clean build bintrayUpload -p tracker-firebase -PbintrayUser=USER -PbintrayKey=KEY -PdryRun=false
   ./gradlew clean build bintrayUpload -p tracker-answers -PbintrayUser=USER -PbintrayKey=KEY -PdryRun=false
   ./gradlew clean build bintrayUpload -p tracker-google-analytics -PbintrayUser=USER -PbintrayKey=KEY -PdryRun=false
   ./gradlew clean build bintrayUpload -p tracker-amplitude -PbintrayUser=USER -PbintrayKey=KEY -PdryRun=false
```