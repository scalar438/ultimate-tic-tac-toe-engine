/*
 * Copyright 2016 riddles.io (developers@riddles.io)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *     For the full copyright and license information, please view the LICENSE
 *     file that was distributed with this source code.
 */

package io.riddles.tictactoe.game.state;

import io.riddles.tictactoe.game.data.TicTacToeBoard;
import io.riddles.tictactoe.game.move.TicTacToeMove;
import org.json.JSONObject;
import io.riddles.javainterface.game.state.AbstractStateSerializer;

/**
 * TicTacToeStateSerializer takes a TicTacToeState and serialises it into a String.
 *
 * @author jim
 */
public class TicTacToeStateSerializer extends AbstractStateSerializer<TicTacToeState> {

    @Override
    public String traverseToString(TicTacToeState state) {
        return visitState(state, false).toString();
    }

    @Override
    public JSONObject traverseToJson(TicTacToeState state) throws NullPointerException {
        return visitState(state, false);
    }

    public JSONObject traverseToJson(TicTacToeState state, Boolean showPossibleMoves) throws NullPointerException {
        return visitState(state, showPossibleMoves);
    }

    private JSONObject visitState(TicTacToeState state, Boolean showPossibleMoves) throws NullPointerException {
        JSONObject stateJson = new JSONObject();
        stateJson.put("round", state.getRoundNumber()-1);
        int moveNr = (state.getRoundNumber()-1)*2+1;
        if (state.getPlayerId() == 1) moveNr++;
        if (showPossibleMoves) moveNr++;
        stateJson.put("move", moveNr);


        //System.out.println(stateJson.get("round") + " " + stateJson.get("move"));

        TicTacToeMove move = state.getPlayerStates().get(0).getMove();
        TicTacToeBoard board = state.getBoard();
        Integer winner = board.getMacroboardWinner();
        String winnerString = "";
        if (winner == null) {
            winnerString = "";
        } else {
            winnerString = String.valueOf(winner);
        }

        if (showPossibleMoves) {
            stateJson.put("field", state.getPossibleMovesPresentationString());
            winnerString = "";
        } else {
            stateJson.put("field", state.getFieldPresentationString());
        }
        stateJson.put("winner", winnerString);


        if (move != null) {
            if (move.getException() == null) {
                stateJson.put("error", "");
            } else {
                stateJson.put("error", move.getException().getMessage());
            }
        }
        return stateJson;
    }
}
