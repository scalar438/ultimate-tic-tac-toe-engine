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

package io.riddles.tictactoe.game.move;

import io.riddles.tictactoe.game.data.MoveType;
import io.riddles.javainterface.exception.InvalidInputException;
import io.riddles.javainterface.serialize.Deserializer;

import java.awt.*;


public class TicTacToeMoveDeserializer implements Deserializer<TicTacToeMove> {

    public TicTacToeMoveDeserializer() {}

    @Override
    public TicTacToeMove traverse(String string) {
        try {
            return visitMove(string);
        } catch (InvalidInputException ex) {
            return new TicTacToeMove(ex);
        } catch (Exception ex) {
            return new TicTacToeMove(new InvalidInputException("Failed to parse move"));
        }
    }

    private TicTacToeMove visitMove(String input) throws InvalidInputException {

        String[] split = input.split(" ");
        if (split.length != 3) {
            throw new InvalidInputException("Number of parameters is incorrect.");
        }
        int column = Integer.parseInt(split[1]);
        int row = Integer.parseInt(split[2]);
        Point c = new Point(column, row);
        return new TicTacToeMove(c);
    }

    public MoveType visitAssessment(String input) throws InvalidInputException {
        switch (input) {
            case "place_move":
                return MoveType.PLACEMOVE;
            default:
                throw new InvalidInputException("Move isn't valid");
        }
    }
}
