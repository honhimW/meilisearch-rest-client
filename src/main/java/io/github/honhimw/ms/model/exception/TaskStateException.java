/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.honhimw.ms.model.exception;

import io.github.honhimw.ms.model.TaskView;
import lombok.Getter;

/**
 * Will be thrown when the task is in the unfinished state while waiting for the task to be finished.
 *
 * @author hon_him
 * @since 2024-03-28
 */

@Getter
public class TaskStateException extends IllegalStateException {

    /**
     * Current task info
     */
    private final TaskView taskView;

    /**
     * Constructs an TaskStateException with no detail message.
     * A detail message is a String that describes this particular exception.
     *
     * @param taskView the task info
     */
    public TaskStateException(TaskView taskView) {
        this.taskView = taskView;
    }

    /**
     * Constructs an TaskStateException with the specified detail
     * message.  A detail message is a String that describes this particular
     * exception.
     *
     * @param taskView the task info
     * @param message  the String that contains a detailed message
     */
    public TaskStateException(TaskView taskView, String message) {
        super(message);
        this.taskView = taskView;
    }

    @Override
    public String getLocalizedMessage() {
        return String.format("task[%d] in state: %s. message: %s", taskView.getUid(), taskView.getStatus(), getMessage());
    }

}
