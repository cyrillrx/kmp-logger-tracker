# Android logger and tracker components
A modular logger component.

# Publish to Bintray

## Logger modules

### Release note
Update the release notes: Logger/release_notes.txt

### Build scripts
```
./gradlew clean build bintrayUpload -p Logger-extension -PbintrayUser=cyrillrx -PbintrayKey=KEY -PdryRun=false
./gradlew clean build bintrayUpload -p Tracker-extension -PbintrayUser=cyrillrx -PbintrayKey=KEY -PdryRun=false
./gradlew clean build bintrayUpload -p Notifier -PbintrayUser=cyrillrx -PbintrayKey=KEY -PdryRun=false
```

### Available scripts
```
../scripts/logger_ext.sh
../scripts/tracker_ext.sh
../scripts/notifier.sh
```