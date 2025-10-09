try:
    from jinja2 import Template
except ImportError:
    raise ImportError(
        "Please install Jinja in order for template generation to succeed"
    )

############################
# Find the repo root
############################

from pathlib import Path


def find_repo(path):
    # Find repository root from the path's parents
    for path in Path(path).parents:
        # Check whether "path/.git" exists and is a directory
        git_dir = path / ".git"
        if git_dir.is_dir():
            return path


# Find the repo root where the script is
repo_root = find_repo(__file__)

############################
# Template README.md
############################
roadmap_path = repo_root / "docs" / "roadmap.md"
with open(roadmap_path, "r") as f:
    # skip first lines since it has the title
    roadmap_contents_lines = f.readlines()[2:]

    # Join back again
    roadmap_contents = "".join(roadmap_contents_lines)

template_path = repo_root / "infra" / "templates" / "README.md.jinja2"
with open(template_path) as f:
    template = Template(f.read())

# Compile template
readme_md = template.render(roadmap_contents=roadmap_contents)

# Add warning to generated file
readme_md = (
    "<!--Do not modify this file. It is auto-generated from a template (infra/templates/README.md.jinja2)-->\n\n"
    + readme_md
)

readme_path = repo_root / "README.md"
with open(readme_path, "w") as f:
    f.write(readme_md)
