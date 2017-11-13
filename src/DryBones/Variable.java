package DryBones;

class Variable<Type extends Comparable<Type>> {

  private String name;
  private Type value;
  private TokenType type;

  private Variable(String newName, Type newValue, TokenType newType) {
    name = newName;
    value = newValue;
    type = newType;
  }

  public String getName() {
    return name;
  }

  public Type getValue() {
    return value;
  }

  public TokenType getType() {
    return type;
  }

  public void setValue(Type newValue) {
    value = newValue;
  }

  static Variable newVariable(TokenType variableType, String variableName) {

    switch (variableType) {
      case INTEGER:
        return new Variable<>(variableName, 0, variableType);
      case FLOAT:
        return new Variable<>(variableName, 0.0f, variableType);
      case STRING:
        return new Variable<>(variableName, "", variableType);
      case BOOLEAN:
        return new Variable<>(variableName, false, variableType);
      default:
        System.err.println("New Variable unknown type");
        System.exit(1);
        return null;
    }
  }

  static Variable newVariable(TokenType variableType, String variableName, String variableData) {
    switch (variableType) {
      case INTEGER:
        return new Variable<>(variableName, Integer.parseInt(variableData), variableType);
      case FLOAT:
        return new Variable<>(variableName, Float.parseFloat(variableData), variableType);
      case STRING:
        return new Variable<>(variableName, variableData, variableType);
      case BOOLEAN:
        return new Variable<>(variableName, Boolean.parseBoolean(variableData), variableType);
      default:
        System.err.println("New Variable unknown type");
        System.exit(1);
        return null;
    }
  }
}
