package autocomplete;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

/**
 * Ternary search tree (TST) implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class TernarySearchTreeAutocomplete implements Autocomplete {
    /**
     * The overall root of the tree: the first character of the first autocompletion term added to this tree.
     */
    private Node overallRoot;

    /**
     * Constructs an empty instance.
     */
    public TernarySearchTreeAutocomplete() {
        overallRoot = null;
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        for (CharSequence term : terms) {
            overallRoot = insert(overallRoot, term, 0);
        }
    }

    private Node insert(Node node, CharSequence term, int depth) {
        char current = term.charAt(depth);

        if (node == null) {
            node = new Node(current);
        }

        if (current < node.data) {
            node.left = insert(node.left, term, depth);
        } else if (current > node.data) {
            node.right = insert(node.right, term, depth);
        } else {
            if (depth < term.length() - 1) {
                node.mid = insert(node.mid, term, depth + 1);
            } else {
                node.isTerm = true;
            }
        }

        return node;
    }


    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> matches = new ArrayList<>();
        Node prefixNode = findPrefixNode(overallRoot, prefix, 0);

        if (prefixNode != null) {
            if (prefixNode.isTerm) {
                matches.add(prefix);
            }

            collectAllMatches(prefixNode.mid, new StringBuilder(prefix), matches);
        }

        return matches;
    }

    private Node findPrefixNode(Node node, CharSequence prefix, int depth) {
        if (node == null) {
            return null;
        }

        char current = prefix.charAt(depth);

        if (current < node.data) {
            return findPrefixNode(node.left, prefix, depth);
        } else if (current > node.data) {
            return findPrefixNode(node.right, prefix, depth);
        } else {
            if (depth < prefix.length() - 1) {
                return findPrefixNode(node.mid, prefix, depth + 1);
            } else {
                return node;
            }
        }
    }

    private void collectAllMatches(Node node, StringBuilder currentPrefix, List<CharSequence> matches) {
        if (node == null) {
            return;
        }

        collectAllMatches(node.left, currentPrefix, matches);

        currentPrefix.append(node.data);
        if (node.isTerm) {
            matches.add(currentPrefix.toString());
        }

        collectAllMatches(node.mid, currentPrefix, matches);
        currentPrefix.deleteCharAt(currentPrefix.length() - 1);

        collectAllMatches(node.right, currentPrefix, matches);
    }


    /**
     * A search tree node representing a single character in an autocompletion term.
     */
    private static class Node {
        private final char data;
        private boolean isTerm;
        private Node left;
        private Node mid;
        private Node right;

        public Node(char data) {
            this.data = data;
            this.isTerm = false;
            this.left = null;
            this.mid = null;
            this.right = null;
        }
    }
}
