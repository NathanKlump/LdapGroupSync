# Table of Contents

- [LDAP Group Sync](#ldap-group-sync)
- [Location](#location)
- [Build processes](#build-processes)

## LDAP Group Sync

This application is a READ **AND** WRITE tool. Its main purpose is to synchronize LDAP groups and ensure that they have the correct members. The `GET` endpoint can be used as a compare tool to see the changes that would occur. The `POST` endpoint will actually update the groups and show the changes.

## Location

This application is on the IAMUTIL servers

## Build Processes

### Local
#### Requirements
1. Ensure you have `EA_PROP_HOME` defined
2. Ensure you have `LdapGroupSync.properties` file in your `EA_PROP_HOME` directory
3. When building/running you will need to use the `-Plocal` flag and have all the required APIs running
#### Build Command
`./gradlew -Plocal build`
