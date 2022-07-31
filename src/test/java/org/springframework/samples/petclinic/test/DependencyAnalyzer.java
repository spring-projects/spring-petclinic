package org.springframework.samples.petclinic.test;

import org.springframework.samples.petclinic.ImpactAnalyzer.connect.Connect;
import org.springframework.samples.petclinic.ImpactAnalyzer.models.HTMLElement;
import org.springframework.samples.petclinic.ImpactAnalyzer.models.TestElement;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DependencyAnalyzer {

	public void dependencyAnalyzer(List<HTMLElement> htmlList, List<TestElement> testList)
	{

		int getMaxVid = Connect.getMaxVid();
		int getPreviousVid = Connect.getPreviousVid();

		for (HTMLElement htmlElement : htmlList) {
			Connect.addHTMLElement(htmlElement.getUrl(), htmlElement.getId(), htmlElement.getValue(), htmlElement.getXpath(), "TypeNotDone", htmlElement.getName(), htmlElement.getClassName(), htmlElement.getTag(), "cssSelectorNotDone",getMaxVid);
		}

		ArrayList<Integer> missingHtmlElements = Connect.getIdOfDifferentHtmlElements(getPreviousVid,getMaxVid,true,true);

		for(TestElement testElement: testList) {
			Connect.addPage(testElement.getUrl(),getMaxVid);

			int pageNameId = Connect.getPageId(testElement.getUrl(),getMaxVid);

			int h_id = Connect.getIdOfHtmlElementByTestElement(testElement,getPreviousVid,getMaxVid,true,false,pageNameId);

			int existTest= Connect.getIdOfAllJavaTestElementWithAccessMethodAndValue(testElement.getAccessMethod(),testElement.getAccessMethodValue());
			if(existTest == 0){
				Connect.addTestElement(testElement.getAccessMethod(), testElement.getAccessMethodValue(), testElement.getActionMethod(), testElement.getActionMethodValue());

			}

			int testId = Connect.getIdOfAllJavaTestElementWithAccessMethodAndValue(testElement.getAccessMethod(),testElement.getAccessMethodValue());
			Connect.addTestElementPageRelation(pageNameId, testId,testElement.getStartingPosition().getLine());
			if(Connect.getDependenciesIdFromTestMethod(testId,testElement.getTestClassName(),testElement.getTestName(),testElement.getStartingPosition().getLine())== 0){
				Connect.addElementDependencies(testId, h_id, testElement.getTestClassName(), testElement.getTestName(), testElement.getStartingPosition().getLine());

			}
		}
		ArrayList<String> pageNames = Connect.getPageNames(getMaxVid);
		for(String pageName: pageNames){
			ArrayList<TestElement> potentialChanges = Connect.getTestElementWhichTestAffectedByDiffHtmlElements(getPreviousVid,getMaxVid,true,true,pageName);
			if (potentialChanges.size()>0) {
				System.out.println("- Change of web elements in " + pageName);
				for (TestElement potentialChange : potentialChanges) {
//					System.out.println("   -Potential change impact on "+potentialChange.getAccessMethodValue() +" line " + potentialChange.getStartingPosition().getLine()+ ", in test class "+ potentialChange.getTestClassName()+", in test "+potentialChange.getTestName() );
					System.out.println("   -Potential change impact on "+potentialChange.getAccessMethodValue() +" line " + potentialChange.getStartingPosition().getLine() );
				}
			}
		}
	}
}

