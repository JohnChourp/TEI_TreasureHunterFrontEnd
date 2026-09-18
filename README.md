Treasure Hunter Mobile

This is the mobile part of the treasure hunter project.
 

## Google Maps key

The map needs a Google Maps Android API key. Put your own in
`app/src/main/res/values/google_maps_api.xml` (the manifest reads it from there),
restricted in the Google Cloud console to this app's package name and signing
certificate. Never commit a real key: this repository is public, and a key stays
readable in git history after it is removed.
