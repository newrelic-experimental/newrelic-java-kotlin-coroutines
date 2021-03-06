package kotlinx.coroutines.scheduling;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Token;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.nr.instrumentation.kotlin.coroutines.NRRunnable;

import kotlin.coroutines.CoroutineContext;

@Weave
public abstract class ExperimentalCoroutineDispatcher {

	public void dispatch(CoroutineContext context, Runnable block) {
		if(!(block instanceof NRRunnable)) {
			Token t = NewRelic.getAgent().getTransaction().getToken();
			if(t != null && t.isActive()) {
				NRRunnable wrapper = new NRRunnable(block, t);
				block = wrapper;
			} else if(t != null) {
				t.expire();
			}
		}
		Weaver.callOriginal();
	}

}
