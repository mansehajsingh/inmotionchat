#!/bin/bash

function sigterm_handler() {
  echo "Caught SIGTERM, passing SIGINT to firebase process."
  firebase emulators:export /tmp/firebase --project in-motion-chat --force # export data to /tmp/firebase
  kill -INT "$firebase_pid" 2>/dev/null # kill firebase emulator
  rm  -rf /var/firebase/data/* # remove everything in import directory
  mv -v /tmp/firebase/* /var/firebase/data/ # move all the new data into the directory that will be used for import on next run
}

trap sigterm_handler SIGTERM

echo "Starting firebase emulators."

firebase emulators:start --project in-motion-chat --import=/var/firebase/data &
firebase_pid=$!

wait "$firebase_pid"