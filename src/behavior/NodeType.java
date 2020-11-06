package behavior;

public enum NodeType {
    DECISION, // has 2 children
    ACTION, // has no child
    DECORATOR, // has 1 child
    COMBINATOR, // has many child, at least 1 child
}

