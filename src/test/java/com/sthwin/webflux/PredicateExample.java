package com.sthwin.webflux;

import java.util.function.Predicate;

/**
 * Created by User
 * Date: 2020. 5. 22. 오후 6:10
 */
public class PredicateExample {

    public static void main(String[] args) {
        Predicate<String> startsWithA = (text) -> text.startsWith("A");
        Predicate<String> endsWithX = (text) -> text.endsWith("x");

        String input = "A hardworking person must relax";
//        Predicate<String> startsWithAAndEndsWithX =
//                (text) -> startsWithA.test(text) && endsWithX.test(text);
//
//        boolean result = startsWithAAndEndsWithX.test(input);

        Predicate<String> composed = startsWithA.and(endsWithX);
        boolean result = composed.test(input);

        System.out.println(result);
    }

}
