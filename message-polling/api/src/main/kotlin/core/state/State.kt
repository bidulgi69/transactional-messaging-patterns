package core.state

enum class State(
    private val order: Int
) {
    PENDING(0),
    ACCEPTED(1),
    REJECTED(2),
}