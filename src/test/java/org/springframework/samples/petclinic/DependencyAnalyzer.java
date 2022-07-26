package org.springframework.samples.petclinic;

import org.springframework.samples.petclinic.ImpactAnalyzer.connect.Connect;
import org.springframework.samples.petclinic.ImpactAnalyzer.models.HTMLElement;
import org.springframework.samples.petclinic.ImpactAnalyzer.models.TestElement;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DependencyAnalyzer {

	public void dependencyAnalyzer(List<HTMLElement> htmlList, List<TestElement> testList)
	{

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Connect.addVersion(timestamp);
		for (HTMLElement htmlElement : htmlList) {
			int vid = Connect.getMaxVid();
			Connect.addHTMLElement(htmlElement.getUrl(), htmlElement.getId(), htmlElement.getValue(), htmlElement.getXpath(), "TypeNotDone", htmlElement.getName(), htmlElement.getClassName(), htmlElement.getTag(), "cssSelectorNotDone",vid);
		}
		int getMaxVid = Connect.getMaxVid();
		int getPreviousVid = Connect.getPreviousVid();
		ArrayList<Integer> missingHtmlElements = Connect.getIdOfDifferentHtmlElements(getPreviousVid,getMaxVid,true,true);

		for(TestElement testElement: testList) {
			int h_id = Connect.getIdOfHtmlElementByTestElement(testElement,getPreviousVid,getMaxVid,true,false);
			int existTest= Connect.getIdOfAllJavaTestElementWithAccessMethodAndValue(testElement.getAccessMethod(),testElement.getAccessMethodValue());
			if(existTest == 0){
				Connect.addTestElement(testElement.getAccessMethod(), testElement.getAccessMethodValue(), testElement.getActionMethod(), testElement.getActionMethodValue());
			}
			if(Connect.getDependenciesIdFromTestMethod(existTest,testElement.getTestClassName(),testElement.getTestName(),testElement.getStartingPosition().getLine())== 0){
				Connect.addElementDependencies(Connect.getMaxTid(), h_id, testElement.getTestClassName(), testElement.getTestName(), testElement.getStartingPosition().getLine());
			}
		}
		ArrayList<String> pageNames = Connect.getPageNames(getMaxVid);
		for(String pageName: pageNames){
			ArrayList<TestElement> potentialChanges = Connect.getTestElementWhichTestAffectedByDiffHtmlElements(getPreviousVid,getMaxVid,true,true,pageName);
			if (potentialChanges.size()>0) {
				System.out.println("- Change of web elements in " + pageName);
				for (TestElement potentialChange : potentialChanges) {
					System.out.println("   -Potential change impact on "+potentialChange.getAccessMethodValue() +" line " + potentialChange.getStartingPosition().getLine()+ ", in test class "+ potentialChange.getTestClassName()+", in test "+potentialChange.getTestName() );
				}
			}
		}
	}
}

