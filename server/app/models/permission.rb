class Permission < ActiveRecord::Base
  validates_uniqueness_of :name
  validates_presence_of :name

  has_many :app_permissions
  has_many :apps, :through => :app_permissions
end
