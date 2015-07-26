# Android libraries
A set of Android libraries.

## Core module
### Logger
### Data binder

## Publish to Bintray

### Release note
Update the release_note.txt

### Core lib script
```
./gradlew clean build bintrayUpload -p Core -PbintrayUser=cyrillrx -PbintrayKey=KEY -PdryRun=false

```