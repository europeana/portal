package eu.europeana.portal2.web.presentation.model;

import eu.europeana.corelib.definitions.exception.ProblemType;
import eu.europeana.portal2.web.presentation.model.abstracts.SearchPageData;

public class ExceptionPage extends SearchPageData {

	private ProblemType problem;

	private String stackTrace;

	private Exception exception;

	public void setProblem(ProblemType problem) {
		this.problem = problem;
	}

	public ProblemType getProblem() {
		return problem;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public Exception getException() {
		return exception;
	}
}
