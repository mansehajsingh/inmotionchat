import { UserCredential, signInWithEmailAndPassword } from "firebase/auth";
import { auth, logErr, logSuccess } from "../imfb";

export default async function login(email: string, password: string): Promise<void> {
    let userCredentials: UserCredential;

    try {
        userCredentials = await signInWithEmailAndPassword(auth, email, password);
    } catch (e) {
        logErr("Failed to sign in: " + e);
        return;
    }

    const accessToken: string = await userCredentials.user.getIdToken(true);
    logSuccess(`Logged in successfully! Acquired id token: ${ accessToken }`);
}