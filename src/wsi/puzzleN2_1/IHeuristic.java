/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wsi.puzzleN2_1;

/**
 *
 * @author Soltys
 */
public interface IHeuristic {
    double computeHeuristic(byte board[][]);
    String getHeuristicName();
}
