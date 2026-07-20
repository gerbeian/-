import Foundation
import SwiftUI

@Observable
class LockViewModel {
    @AppStorage("lockEnabled") var lockEnabled: Bool = false
    var isLocked: Bool = false
    var enteredPasscode: String = ""
    var errorMessage: String = ""
    var showError: Bool = false

    private let passcodeKey = "zhengqi_passcode"

    func checkLockStatus() -> Bool {
        return lockEnabled
    }

    func hasPasswordSet() -> Bool {
        return KeychainManager.shared.read(key: passcodeKey) != nil
    }

    func setPassword(_ password: String) -> Bool {
        let hash = KeychainManager.shared.hashPassword(password)
        return KeychainManager.shared.save(key: passcodeKey, value: hash)
    }

    func verifyPassword(_ password: String) -> Bool {
        guard let stored = KeychainManager.shared.read(key: passcodeKey) else { return false }
        let hash = KeychainManager.shared.hashPassword(password)
        return stored == hash
    }

    func deletePassword() -> Bool {
        return KeychainManager.shared.delete(key: passcodeKey)
    }

    func addDigit(_ digit: String) {
        if enteredPasscode.count < 6 {
            enteredPasscode += digit
        }
    }

    func removeDigit() {
        if !enteredPasscode.isEmpty {
            enteredPasscode.removeLast()
        }
    }

    func clearEntry() {
        enteredPasscode = ""
        showError = false
    }

    func attemptUnlock() -> Bool {
        if verifyPassword(enteredPasscode) {
            isLocked = false
            enteredPasscode = ""
            showError = false
            return true
        } else {
            showError = true
            errorMessage = "密码错误，请重试"
            enteredPasscode = ""
            return false
        }
    }
}