#!/usr/bin/env python3
"""
Converts Maven Surefire XML test reports to a single JSON file.
Reads all TEST-*.xml files from target/surefire-reports/ and converts them to JSON.
"""
import json
import os
import sys
import xml.etree.ElementTree as ET
from pathlib import Path


def find_project_root():
    """Find the project root directory by looking for pom.xml or build.gradle."""
    current = Path(__file__).resolve().parent
    while current != current.parent:
        if (current / 'pom.xml').exists() or (current / 'build.gradle').exists():
            return current
        current = current.parent
    # Fallback to parent of jfrog/convert
    return Path(__file__).resolve().parent.parent.parent


def parse_test_suite(xml_file):
    """Parse a single TEST-*.xml file and return test suite data."""
    try:
        tree = ET.parse(xml_file)
        root = tree.getroot()
        
        suite_name = root.get('name', xml_file.stem.replace('TEST-', ''))
        tests = int(root.get('tests', 0))
        failures = int(root.get('failures', 0))
        errors = int(root.get('errors', 0))
        skipped = int(root.get('skipped', 0))
        time = float(root.get('time', 0.0))
        
        test_cases = []
        for testcase in root.findall('testcase'):
            test_case = {
                'name': testcase.get('name', ''),
                'classname': testcase.get('classname', ''),
                'time': float(testcase.get('time', 0.0))
            }
            
            failure = testcase.find('failure')
            if failure is not None:
                test_case['failure'] = {
                    'message': failure.get('message', ''),
                    'type': failure.get('type', ''),
                    'content': failure.text or ''
                }
            
            error = testcase.find('error')
            if error is not None:
                test_case['error'] = {
                    'message': error.get('message', ''),
                    'type': error.get('type', ''),
                    'content': error.text or ''
                }
            
            skipped_elem = testcase.find('skipped')
            if skipped_elem is not None:
                test_case['skipped'] = skipped_elem.text or ''
            
            test_cases.append(test_case)
        
        return {
            'name': suite_name,
            'tests': tests,
            'failures': failures,
            'errors': errors,
            'skipped': skipped,
            'time': time,
            'testCases': test_cases
        }
    except Exception as e:
        print(f"Error parsing {xml_file}: {e}", file=sys.stderr)
        return None


def main():
    # Find project root
    project_root = find_project_root()
    reports_dir = project_root / 'target' / 'surefire-reports'
    
    # Determine output file (default to project root, or use command line arg)
    if len(sys.argv) > 1:
        output_file = Path(sys.argv[1])
    else:
        output_file = project_root / 'test-results.json'
    
    if not reports_dir.exists():
        print(f"Reports directory does not exist: {reports_dir}", file=sys.stderr)
        sys.exit(1)
    
    test_results = []
    total_tests = 0
    total_failures = 0
    total_errors = 0
    total_skipped = 0
    total_time = 0.0
    
    # Find all TEST-*.xml files
    xml_files = sorted(reports_dir.glob('TEST-*.xml'))
    
    if not xml_files:
        print(f"No TEST-*.xml files found in {reports_dir}", file=sys.stderr)
        sys.exit(1)
    
    print(f"Found {len(xml_files)} XML test report file(s)...")
    
    for xml_file in xml_files:
        suite = parse_test_suite(xml_file)
        if suite:
            test_results.append(suite)
            total_tests += suite['tests']
            total_failures += suite['failures']
            total_errors += suite['errors']
            total_skipped += suite['skipped']
            total_time += suite['time']
    
    # Create JSON output
    json_output = {
        'summary': {
            'totalTests': total_tests,
            'totalFailures': total_failures,
            'totalErrors': total_errors,
            'totalSkipped': total_skipped,
            'totalTime': total_time
        },
        'suites': test_results
    }
    
    # Write JSON file
    output_file.parent.mkdir(parents=True, exist_ok=True)
    with open(output_file, 'w') as f:
        json.dump(json_output, f, indent=2)
    
    print(f"\nTest results converted to JSON: {output_file}")
    print(f"Summary: {total_tests} tests, {total_failures} failures, {total_errors} errors, {total_skipped} skipped")
    print(f"Processed {len(test_results)} test suite(s)")


if __name__ == '__main__':
    main()

