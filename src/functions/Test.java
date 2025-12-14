package functions;

public class Test {
    public static void main(String[] args) throws InappropriateFunctionPointException {
        double[] yValues = {1, 4, 9};
        ArrayTabulatedFunction arrayFunc = new ArrayTabulatedFunction(0, 2, yValues);

        FunctionPoint[] points = {
                new FunctionPoint(0, 1),
                new FunctionPoint(1, 4),
                new FunctionPoint(2, 9)
        };
        LinkedListTabulatedFunction listFunc = new LinkedListTabulatedFunction(points);

        //Проверка toString()
        System.out.println("ArrayTabulatedFunction: " + arrayFunc);
        System.out.println("LinkedListTabulatedFunction: " + listFunc);

        //Проверка equals()
        ArrayTabulatedFunction arrayCopy = (ArrayTabulatedFunction) arrayFunc.clone();
        LinkedListTabulatedFunction listCopy = (LinkedListTabulatedFunction) listFunc.clone();

        System.out.println("arrayFunc.equals(arrayCopy): " + arrayFunc.equals(arrayCopy)); // true
        System.out.println("listFunc.equals(listCopy): " + listFunc.equals(listCopy));       // true
        System.out.println("arrayFunc.equals(listFunc): " + arrayFunc.equals(listFunc));     // false

        //Проверка hashCode()
        System.out.println("arrayFunc.hashCode(): " + arrayFunc.hashCode());
        System.out.println("arrayCopy.hashCode(): " + arrayCopy.hashCode());
        System.out.println("listFunc.hashCode(): " + listFunc.hashCode());
        System.out.println("listCopy.hashCode(): " + listCopy.hashCode());

        arrayFunc.setPointY(1, 4.002);
        System.out.println("После изменения arrayFunc.hashCode(): " + arrayFunc.hashCode());

        //Проверка clone()
        arrayCopy.setPointY(0, 10);
        listCopy.setPointY(0, 20);

        System.out.println("Исходный arrayFunc: " + arrayFunc);
        System.out.println("Клон arrayCopy: " + arrayCopy);

        System.out.println("Исходный listFunc: " + listFunc);
        System.out.println("Клон listCopy: " + listCopy);
    }
}
