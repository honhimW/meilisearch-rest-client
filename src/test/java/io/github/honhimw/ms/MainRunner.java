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

package io.github.honhimw.ms;

/**
 * @author hon_him
 * @since 2024-01-02
 */

public class MainRunner {

    public static void main(String[] args) {
        String str = "search,Provides access to both POST and GET search endpoints\n" +
            "documents.add,Provides access to the add documents and update documents endpoints\n" +
            "documents.get,Provides access to the get one document, get documents with POST, and get documents with GET endpoints endpoints\n" +
            "documents.delete,Provides access to the delete one document, delete all documents, batch delete, and delete by filter endpoints\n" +
            "indexes.create,Provides access to the create index endpoint\n" +
            "indexes.get,Provides access to the get one index and list all indexes endpoints. Non-authorized indexes will be omitted from the response\n" +
            "indexes.update,Provides access to the update index endpoint\n" +
            "indexes.delete,Provides access to the delete index endpoint\n" +
            "indexes.swap,Provides access to the swap indexes endpoint. Non-authorized indexes will not be swapped\n" +
            "tasks.get,Provides access to the get one task and get tasks endpoints. Tasks from non-authorized indexes will be omitted from the response\n" +
            "tasks.cancel,Provides access to the cancel tasks endpoint. Tasks from non-authorized indexes will not be canceled\n" +
            "tasks.delete,Provides access to the delete tasks endpoint. Tasks from non-authorized indexes will not be deleted\n" +
            "settings.get,Provides access to the get settings endpoint and equivalents for all subroutes\n" +
            "settings.update,Provides access to the update settings and reset settings endpoints and equivalents for all subroutes\n" +
            "stats.get,Provides access to the get stats of an index endpoint and the get stats of all indexes endpoint. For the latter, non-authorized indexes are omitted from the response\n" +
            "dumps.create,Provides access to the create dump endpoint. Not restricted by indexes\n" +
            "snapshots.create,Provides access to the create snapshot endpoint. Not restricted by indexes\n" +
            "version,Provides access to the get Meilisearch version endpoint\n" +
            "keys.get,Provides access to the get all keys endpoint\n" +
            "keys.create,Provides access to the create key endpoint\n" +
            "keys.update,Provides access to the update key endpoint\n" +
            "keys.delete,Provides access to the delete key endpoint";
        String[] split = str.split("\n");
        for (String string : split) {
            String[] split1 = string.split(",", 2);
            String string1 = split1[0];
            String string2 = split1[1];
            String name = string1.replaceAll("\\.", "_").toUpperCase();
            System.out.println(String.format(String.format("%s(\"%s\", \"%s\"),", name, string1, string2)));
        }
    }

}
