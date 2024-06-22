package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Binary search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class BinarySearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> elements;
    /**
     * Constructs an empty instance
     */
    public BinarySearchAutocomplete() {
        elements = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {

        this.elements.addAll(terms);
        //Entire list is resorted using Collections.sort
        Collections.sort(elements, CharSequence::compare);
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        //Creates an ArrayList of CharSequences containing matches
        List<CharSequence> matchList = new ArrayList<>();
        //Reports a value to check if it's below or above the middle index
        int current = Collections.binarySearch(elements, prefix, CharSequence::compare);
        //elements:List being searched
        //prefix:key being searched
        //CharSequence::compare the comparator by which the list is ordered
        if (current < 0) {
            current = -(current + 1);
        }
        for (int i = current; i < elements.size(); i++) {
            //Stores the element at index in term
            CharSequence term = elements.get(i);
            //Checks if the term start with the prefix
            //Breaks out of loop if the element doesn't start with prefix
            if (term.toString().startsWith(prefix.toString())) {
                matchList.add(term);//Adds the matching term
            } else{
                break;
            }

        }
        return matchList;
    }
}



