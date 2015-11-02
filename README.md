# Android lrx logger
A modular logger component.

# Publish to Bintray

## Logger module

### Release note
Update the release_note.txt

### Build script
```
./gradlew clean build bintrayUpload -p Logger -PbintrayUser=cyrillrx -PbintrayKey=KEY -PdryRun=false

```