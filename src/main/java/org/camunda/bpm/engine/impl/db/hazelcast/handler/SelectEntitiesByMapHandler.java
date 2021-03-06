/* Licensed under the Apache License, Version 2.0 (the "License");
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

package org.camunda.bpm.engine.impl.db.hazelcast.handler;

import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.impl.db.DbEntity;
import org.camunda.bpm.engine.impl.db.ListQueryParameterObject;
import org.camunda.bpm.engine.impl.db.hazelcast.HazelcastPersistenceSession;

import com.hazelcast.query.SqlPredicate;

/**
 * @author Sebastian Menski
 */
public class SelectEntitiesByMapHandler extends AbstractSelectEntitiesStatementHandler {

  public SelectEntitiesByMapHandler(Class<? extends DbEntity> type) {
    super(type);
  }

  public List<?> execute(HazelcastPersistenceSession session, Object parameter) {
    Map<String, Object> parameterMap = getParameterMap(parameter);

    SqlPredicate predicate = null;
    if (parameterMap != null && !parameterMap.isEmpty()) {
      predicate = SqlPredicateFactory.createAndPredicate(parameterMap);
    }

    List<?> entities = selectByPredicate(session, predicate);
    return filterEntities(parameter, entities);
  }

  @SuppressWarnings("unchecked")
  protected Map<String, Object> getParameterMap(Object parameter) {
    if (parameter instanceof ListQueryParameterObject) {
      // TODO: add limit support
      return (Map<String, Object>) ((ListQueryParameterObject) parameter).getParameter();
    }
    else {
      return (Map<String, Object>) parameter;
    }
  }

  protected List<?> filterEntities(Object parameter, List<?> entities) {
    return entities;
  }

}
