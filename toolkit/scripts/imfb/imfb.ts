import {Command, program} from "commander";
import {FirebaseApp, initializeApp} from "firebase/app";
import {connectAuthEmulator, getAuth} from "firebase/auth";
import {default as firebaseConfig} from "./firebase.conf.json"; // Obtain from Firebase admin console
import login from "./commands/login";

interface ImfbCommand {
    name: string,
    description: string,
    action: (...args: any[]) => void,
    arguments?: string,
}

export const firebaseApp: FirebaseApp = initializeApp(firebaseConfig);

export const auth = getAuth();
connectAuthEmulator(auth, "http://localhost:9099");

// Each command and its respective handler to pass to commander js
const commandsAndHandlers: ImfbCommand[] = [
    {
        name: "login",
        description: "Log in with a Firebase email password user, receiving id token periodically when requested.",
        action: login,
        arguments: "<email> <password>"
    },
];

// Start!
main();

function main() {
    program
        .description("A command line utility for managing firebase without a UI.");

    commandsAndHandlers.forEach(cmd => {
        const commanderCommand: Command = program
            .command(cmd.name)
            .description(cmd.description)
            .action(cmd.action);

        if (cmd.arguments) {
            commanderCommand.arguments(cmd.arguments);
        }
    });

    program.parse(process.argv);
}

export function logInfo(info: any): void {
    _log("INFO", info)
}

export function logSuccess(success: any): void {
    _log("SUCCESS", success);
}

export function logErr(error: any): void {
    _log("ERROR", error, true);
}

function _log(type: string, message: string, error = false): void {
    if (error) {
        console.error(`[${type}] ${message}`);
    } else {
        console.log(`[${type}] ${message}`);
    }
}